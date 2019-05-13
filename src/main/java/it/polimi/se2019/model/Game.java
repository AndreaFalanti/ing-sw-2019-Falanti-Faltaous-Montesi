package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.NormalTile;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.board.Tile;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.util.Jsons;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Game {
    private Board mBoard;
    private List<Player> mPlayers;
    private Deck<PowerUpCard> mPowerUpCardDeck;
    private Deck<Weapon> mWeaponDeck;
    private Deck<AmmoCard> mAmmoCardDeck;
    private int mTurnNumber;
    private int mSkullNum;
    private List<PlayerColor> mKills = new ArrayList<>();
    private List<PlayerColor> mOverkills = new ArrayList<>();
    private boolean mFinalFrenzy;
    private Integer mFinalFrenzyTurnStart;
    private int mActivePlayerIndex;
    private int mRemainingActions;
    private boolean mFirstPlayerDoneFinalFrenzy = false;

    //same for total kills and player kill points distribution
    private static final int[] KILLS_VALUE = {8, 6, 4, 2, 1, 1};
    private static final int[] FLIPPED_PLAYER_VALUE = {2, 1, 1, 1};

    /**
     *
     * @param board Board selected for the game
     * @param players List of players
     * @param killsToFinish Number of kills before triggering final frenzy
     * @throws IllegalArgumentException Thrown if board or players are null,
     *          player size is < 3 or killsNum is negative
     */
    public Game(Board board, List<Player> players, int killsToFinish) {
        if (board == null || players == null || killsToFinish <= 0 || players.size() < 3) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = i + 1; j < players.size(); j++) {
                if (players.get(i).getColor() == players.get(j).getColor()) {
                    throw new IllegalArgumentException("Two or more player have same player color");
                }
            }
        }

        mBoard = board;
        mPlayers = players;
        mSkullNum = killsToFinish;

        mFinalFrenzy = false;
        //set active player the last one in list so that at first turn it actually set it to 0
        mActivePlayerIndex = mPlayers.size() - 1;
        mTurnNumber = 0;

        List<AmmoCard> ammoCards = AmmoCard.returnDeckFromJson(Jsons.get("AmmoCardDeck"));
        mAmmoCardDeck = new Deck<>(ammoCards);

        List<PowerUpCard> powerUpCards = PowerUpCard.returnDeckFromJson(Jsons.get("PowerUpCardDeck"));
        mPowerUpCardDeck = new Deck<>(powerUpCards);

        List<Weapon> weaponCards = Weapon.returnDeckFromJson(Jsons.get("WeaponDeck"));
        mWeaponDeck = new Deck<>(weaponCards, false);

        refillAmmoTiles();
        fillSpawnsWithWeapons();
    }

    //region GETTERS
    public int getTurnNumber() {
        return mTurnNumber;
    }

    public Deck<AmmoCard> getAmmoCardDeck() {
        return mAmmoCardDeck;
    }

    public Deck<Weapon> getWeaponDeck() {
        return mWeaponDeck;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public Deck<PowerUpCard> getPowerUpDeck() {
        return mPowerUpCardDeck;
    }

    public Board getBoard() {
        return mBoard;
    }

    public int getSkullNum() {
        return mSkullNum;
    }

    public List<PlayerColor> getKills() {
        return mKills;
    }

    public List<PlayerColor> getOverkills() {
        return mOverkills;
    }

    public int getActivePlayerIndex() {
        return mActivePlayerIndex;
    }

    public Player getActivePlayer () {
        return mPlayers.get(mActivePlayerIndex);
    }

    public boolean isFinalFrenzy () {
        return mFinalFrenzy;
    }

    public Integer getFinalFrenzyTurnStart () {
        return mFinalFrenzyTurnStart;
    }

    public int getRemainingActions () {
        return mRemainingActions;
    }

    public boolean hasFirstPlayerDoneFinalFrenzy () {
        return mFirstPlayerDoneFinalFrenzy;
    }
    //endregion

    /**
     * Start next turn, incrementing turn number, changing active player and setting number of actions.
     * If it's game over, distribute kill points to players and finish the game.
     */
    public void startNextTurn() {
        mTurnNumber++;
        if (isGameOver()) {
            distributeTotalKillsScore();
            return;
        }

        for (Player player : mPlayers) {
            if (player.isDead()) {
                //TODO: notify respawn to dead player, that will choose respawn position from
                // its view and call controller
                return;
            }
        }

        if (mActivePlayerIndex >= mPlayers.size() - 1) {
            mActivePlayerIndex = 0;
        }
        else {
            mActivePlayerIndex++;
        }

        mRemainingActions = calculateTurnActions();

        //TODO: message to all clients containing new active player and turn number
    }

    /**
     * Calculate if game is finished (all players have done their final frenzy rounds).
     * @return true if final frenzy is over, false if not
     */
    public boolean isGameOver() {
        // example: finalFrenzy is triggered on turn 4 and 3 player are present,
        // at turn 4 + 3 + 1 = 8 the game is over (last turn is 7).
        return mFinalFrenzyTurnStart != null
                && mTurnNumber >= mFinalFrenzyTurnStart + mPlayers.size() + 1;
    }

    /**
     * Add a kill to kills list. If skullNum is reached, starts final frenzy.
     * @param killer Player color of the killer
     */
    private void registerKill (PlayerColor killer) {
        mKills.add(killer);

        if (mKills.size() == mSkullNum) {
            setFinalFrenzyStatus();
        }
    }

    /**
     * Add an overkill to overkills list.
     * @param killer Player color of the killer
     */
    private void registerOverkill (PlayerColor killer) {
        mOverkills.add(killer);
    }

    /**
     * Return a player from given color.
     * @param color Color of the player to return
     * @return Player with given color
     */
    public Player getPlayerFromColor (PlayerColor color) {
        for (Player player : mPlayers) {
            if (player.getColor() == color) {
                return player;
            }
        }

        throw new IllegalArgumentException("Can't find player with color: " + color);
    }

    /**
     * Set final frenzy status to true and set starting turn of final frenzy.
     */
    private void setFinalFrenzyStatus () {
        mFinalFrenzy = true;
        mFinalFrenzyTurnStart = mTurnNumber;
    }

    /**
     * Get number of actions that active player can do this turn.
     * @return number of actions
     */
    private int calculateTurnActions () {
        if (isFinalFrenzy() && mActivePlayerIndex == 0) {
            mFirstPlayerDoneFinalFrenzy = true;
            return 1;
        }
        else if (isFinalFrenzy() && mFirstPlayerDoneFinalFrenzy) {
            return 1;
        }
        else {
            return 2;
        }
    }

    /**
     * Give players bonus score points based on kills and overkills done during the game.
     */
    private void distributeTotalKillsScore() {
        PlayerColor[] colorArray = mKills.toArray(new PlayerColor[0]);
        if (!mOverkills.isEmpty()) {
            PlayerColor[] colorArray2 = mOverkills.toArray(new PlayerColor[0]);
            PlayerColor[] totalKilltrack = Stream.concat(Arrays.stream(colorArray), Arrays.stream(colorArray2))
                    .toArray(PlayerColor[]::new);

            scoreCalculation(totalKilltrack, KILLS_VALUE, 0, false);
        }
        else {
            scoreCalculation(colorArray, KILLS_VALUE, 0, false);
        }
    }

    /**
     * Distribute score to players that have done damage to killed player.
     * @param deadPlayerColor Dead player's color
     */
    private void distributePlayerKillScore (PlayerColor deadPlayerColor) {
        Player deadPlayer = getPlayerFromColor(deadPlayerColor);
        boolean boardFlipped = deadPlayer.isBoardFlipped();
        int[] scoreValues = (boardFlipped) ? FLIPPED_PLAYER_VALUE : KILLS_VALUE;
        int scoreOffset = (boardFlipped) ? 0 : deadPlayer.getDeathsNum();
        scoreCalculation(deadPlayer.getDamageTaken(), scoreValues, scoreOffset, !boardFlipped);
    }

    /**
     * Calculate score and give it to players.
     * @param colors Array of PlayerColor to iterate for score calculation
     * @param scoreValues Score values to apply
     * @param scoreStartingPos Offset in score values, 0 if not present
     * @param firstBloodBonus Give bonus point for first blood?
     */
    private void scoreCalculation (PlayerColor[] colors, int[] scoreValues, int scoreStartingPos,
                                   boolean firstBloodBonus) {
        if (firstBloodBonus) {
            getPlayerFromColor(colors[0]).addScore(1);
        }

        Map<PlayerColor, Integer> map = new EnumMap<>(PlayerColor.class);
        for (PlayerColor color : colors) {
            // last color could be null if player is not overkilled
            if (color != null) {
                if (map.containsKey(color)) {
                    map.put(color, map.get(color) + 1);
                }
                else {
                    map.put(color, 1);
                }
            }
        }
        AtomicInteger i = new AtomicInteger(scoreStartingPos);
        map
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEachOrdered( e -> {getPlayerFromColor(e.getKey()).addScore(scoreValues[i.get()]);
                    i.getAndIncrement();});
    }

    /**
     * Handle game logic about damage action, updating damage taken by target and registering a kill if player is dead.
     * @param shooter Player color of the shooter
     * @param target Player color of the target
     * @param damage Damage done to target
     */
    public void handleDamageInteraction(PlayerColor shooter, PlayerColor target, Damage damage) {
        Player targetPlayer = getPlayerFromColor(target);

        targetPlayer.onDamageTaken(damage, shooter);
    }

    /**
     * Called on turn end, handle all mechanics and rules that activate at turn's end.
     */
    public void onTurnEnd () {
        // counter that indicates how many kills the active player has done
        int killsScored = 0;
        for (Player player : mPlayers) {
            if (player.isDead()) {
                killsScored++;
                registerKill(getActivePlayer().getColor());
                if (player.isOverkilled()) {
                    registerOverkill(getActivePlayer().getColor());
                    getActivePlayer().onDamageTaken(new Damage(0, 1), player.getColor());
                }
                distributePlayerKillScore(player.getColor());
                // Remove temporarily player from board
                player.move(null);
            }
        }

        // extra point for double kill or multiple kills.
        if (killsScored >= 2) {
            getActivePlayer().addScore(1);
        }

        //player with no damage or respawned player will always have board flipped
        if (mFinalFrenzy) {
            for (Player player : mPlayers) {
                if (player.hasNoDamage()) {
                    player.flipBoard();
                }
            }
        }

        refillAmmoTiles();
    }

    /**
     * Get current player leaderboard, based on score.
     * @return player list ordered by decreasing score
     */
    public List<Player> getLeaderboard () {
        return mPlayers.stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Player::getScore)))
                .collect(Collectors.toList());
    }

    /**
     * Get game winner.
     * @return Player that won the game
     */
    public Player getWinner () {
        Player winner = mPlayers.get(0);

        for (Player player : mPlayers) {
            if (player.getScore() > winner.getScore()) {
                winner = player;
            }
        }

        return winner;
    }

    public void decreaseActionCounter () {
        mRemainingActions--;
    }

    private void refillAmmoTiles () {
        for (Tile tile : mBoard.getTiles()){
            if (tile != null && tile.getTileType().equals("normal")) {
                NormalTile normalTile = (NormalTile) tile;
                if (normalTile.getAmmoCard() == null) {
                    normalTile.setAmmoCard(getAmmoCardDeck().drawCard());
                }
            }
        }
    }

    /**
     * Called only on game start, fill spawn tiles with weapons
     */
    private void fillSpawnsWithWeapons () {
        for (Tile tile : mBoard.getTiles()){
            if (tile != null && tile.getTileType().equals("spawn")) {
                SpawnTile spawnTile = (SpawnTile) tile;
                // all weapons are null at start, fill them with weapon references
                for (int i = 0; i < SpawnTile.MAX_WEAPONS; i++) {
                    spawnTile.addWeapon(mWeaponDeck.drawCard());
                }
            }
        }
    }
}
