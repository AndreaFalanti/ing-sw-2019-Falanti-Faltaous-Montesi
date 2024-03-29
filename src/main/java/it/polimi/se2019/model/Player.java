package it.polimi.se2019.model;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.update.*;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.util.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Representation of players in the game
 *
 * @author Andrea Falanti, Abanoub Faltaous
 */
public class Player extends Observable<Update> {
    private AmmoValue mAmmo;
    private PowerUpCard[] mPowerUpCards = new PowerUpCard[4];
    private Weapon[] mWeapons = new Weapon[3];
    private PlayerColor mColor;
    private int mDeathsNum = 0;
    private PlayerColor[] mDamageTaken = new PlayerColor[12];
    private Map<PlayerColor, Integer> mMarks = new EnumMap<>(PlayerColor.class);
    private int mScore=0;
    private Position mPos;
    private String mName;
    private boolean mDead = false;
    private boolean mBoardFlipped = false;
    private static final int MAX_MARKS = 3;

    private void initializeMarksMap () {
        mMarks.put(PlayerColor.YELLOW,0);
        mMarks.put(PlayerColor.GREEN,0);
        mMarks.put(PlayerColor.PURPLE,0);
        mMarks.put(PlayerColor.BLUE,0);
        mMarks.put(PlayerColor.GREY,0);
    }

    public Player (String name, PlayerColor color, Position pos, AmmoValue ammo) {
        mName = name;
        mColor = color;
        mPos = pos;
        mAmmo = ammo;
        initializeMarksMap();
    }

    public Player (String name, PlayerColor color, Position pos) {
        this(name, color, pos, new AmmoValue(1, 1 , 1));
    }

    public Player (String name, PlayerColor color) {
        this(name, color, null);
    }

    //region GETTERS
    public boolean isSpawned() {
        return mPos != null;
    }

    public AmmoValue getAmmo() {
        return mAmmo;
    }

    public PlayerColor[] getDamageTaken() {
        return mDamageTaken;
    }

    public Map<PlayerColor, Integer> getMarks() {
        return mMarks;
    }

    public PlayerColor getColor() {
        return mColor;
    }

    public int getDeathsNum() {
        return mDeathsNum;
    }

    public Weapon[] getWeapons() {
        return mWeapons;
    }

    public boolean isDead() {
        return mDead;
    }

    public PowerUpCard[] getPowerUps() {
        return mPowerUpCards;
    }

    public int getScore() {
        return mScore;
    }

    public Position getPos() {
        return mPos;
    }

    public String getName() {
        return  mName;
    }

    public boolean isBoardFlipped() {
        return mBoardFlipped;
    }

    public Weapon getWeapon(int index) {
        return mWeapons[index];
    }

    public PowerUpCard getPowerUpCard(int index) {
        return mPowerUpCards[index];
    }

    //endregion

    //region SETTERS

    public Player setAmmo(AmmoValue ammo) {
        mAmmo = ammo;
        notify(new PlayerAmmoUpdate(mColor, mAmmo));
        return this;
    }

    public void setWeapons(Weapon[] weapons) {
        mWeapons = weapons;
        notify(new PlayerWeaponsUpdate(mColor, mWeapons));
    }

    public void setPowerUpCards(PowerUpCard[] powerUpCards) {
        mPowerUpCards = powerUpCards;
        notify(new PlayerPowerUpsUpdate(mColor, mPowerUpCards));
    }

    public void setDamageTaken(PlayerColor[] damageTaken) {
        mDamageTaken = damageTaken;
        notify(new PlayerDamageUpdate(mColor, mDamageTaken));
    }

    public void setMarks(Map<PlayerColor, Integer> marks) {
        // This maps have same PlayerColor set, so can optimize notifies by checking if values are actually changed
        for (Map.Entry<PlayerColor, Integer> entry : marks.entrySet()) {
            if (!marks.get(entry.getKey()).equals(mMarks.get(entry.getKey()))) {
                notify(new PlayerMarksUpdate(mColor, mMarks.get(entry.getKey()), entry.getKey()));
            }
        }

        mMarks = marks;
    }

    public void setPos(Position pos) {
        mPos = pos;
    }

    public void setDead(boolean dead) {
        mDead = dead;
    }

    //endregion

    public void flipBoard () {
        mBoardFlipped = true;

        notify(new PlayerBoardFlipUpdate(mColor));
    }

    public boolean isOverkilled () {
        return mDamageTaken[11] != null;
    }

    public boolean hasNoDamage () {
        return mDamageTaken[0] == null;
    }

    /**
     * return if player has reached the maximum number of weapon in his hand
     * @return true if has three weapons, false otherwise
     */
    public boolean isFullOfWeapons () {
        for (Weapon weapon : mWeapons) {
            if (weapon == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * return if player has reached the maximum number of power up cards in his hand
     * @return true if has three power ups, false otherwise
     */
    public boolean isFullOfPowerUps () {
        for (int i = 0; i < mPowerUpCards.length - 1; i++) {
            if (getPowerUpCard(i) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * takes a weapon (is used in Action class to exchange weapon when the hand of player is full)
     * @param index is the index of the weapon to add in your hand
     * @return Weapon taken from player
     */
    public Weapon takeWeapon (int index) {
        Weapon weapon = mWeapons[index];
        mWeapons[index] = null;
        return weapon;
    }

    /**
     * Get max grab distance of player. If it has at least 3 damage, unlock "adrenaline grab".
     * @return 1 if damage is minor of 3, 2 if major or equal to 3
     */
    public int getMaxGrabDistance () {
        return (mDamageTaken[2] != null) ? 2 : 1;
    }

    /**
     * Return if player can perform "adrenaline move and shoot".
     * @return true if has at least 6 damage, false otherwise
     */
    public boolean canMoveBeforeShooting () {
        return mDamageTaken[5] != null;
    }

    /**
     * Update score of a player
     * @param value is the value to add to the current score
     */
    public void addScore(int value) {
        mScore += value;
    }

    public void addAmmo (AmmoValue ammoValue) {
        mAmmo.add(ammoValue);
        notify(new PlayerAmmoUpdate(mColor, mAmmo));
    }

    public void payAmmo (AmmoValue ammoValue) {
        mAmmo.subtract(ammoValue);
        notify(new PlayerAmmoUpdate(mColor, mAmmo));
    }

    /**
     * Update the damage taken of a player
     * @param attackingPlayer is player that attacks
     * @param damage value of damage to add to the current damage
     */
    private void sufferedDamage(PlayerColor attackingPlayer, int damage) {
        if (damage != 0) {
            damage += getMarks().get(attackingPlayer);
            mMarks.put(attackingPlayer, 0);
        }

        for (int i = 0; i < mDamageTaken.length && damage > 0; i++) {
            if (mDamageTaken[i] == null) {
                mDamageTaken[i] = attackingPlayer;
                damage--;
            }
        }

        notify(new PlayerDamageUpdate(mColor, mDamageTaken));
    }

    /**
     * Update the marks on a player
     * @param attackingPlayer is player that attacks
     * @param marks value of marks to add to the current marks
     */
    private void sufferedMarks(PlayerColor attackingPlayer,int marks) {
        if(marks + getMarks().get(attackingPlayer) >= MAX_MARKS) {
            mMarks.put(attackingPlayer, MAX_MARKS);
        }
        else{
            mMarks.put(attackingPlayer,marks);
        }

        notify(new PlayerMarksUpdate(mColor, mMarks.get(attackingPlayer), attackingPlayer));
    }

    /**
     * Increase the numbers of deaths of player
     */
    private void incrementDeaths() {
        mDeathsNum += 1;
    }

    /**
     * Add a weapon to player hand and throws exception in case player hand is full
     * @param value is the weapon to add
     */
    public void addWeapon(Weapon value) {
        int i=0;

        while(i <= mWeapons.length - 1 && mWeapons[i] != null )
            i++;
        if(i <= mWeapons.length -1) {
            mWeapons[i] = value;
            notify(new PlayerWeaponsUpdate(mColor, mWeapons));
        }
        else {
            throw new FullHandException("You have reached the maximum number of weapons in your hand");
        }
    }

    /**
     * set player in death status
     */
    private void setDeadStatus(){
        mDead = mDamageTaken[10] != null;
    }

    /**
     * add powerup card in player hand and throw exception when player reaches the maximum number of powerups card
     * @param value powerup card to add
     * @param isRespawn boolean value to know if a player could have four powerups instead of three
     * @return modified player
     */
    public Player addPowerUp(PowerUpCard value, boolean isRespawn) {
        int lengthToCheck = isRespawn ? mPowerUpCards.length : (mPowerUpCards.length - 1);
        for (int i = 0; i < lengthToCheck; i++) {
            if (mPowerUpCards[i] == null) {
                mPowerUpCards[i] = value;

                notify(new PlayerPowerUpsUpdate(mColor, mPowerUpCards));
                return this;
            }
        }

        throw new FullHandException ("PowerUp hand is full, can't draw another card");
    }

    public Player addPowerUp (PowerUpCard value) {
        return addPowerUp(value, false);
    }

    /**
     * discard a card from the hand of player
     * @param card card to discard
     */
    public void discard(PowerUpCard card) {
        for (int i = 0; i < mPowerUpCards.length; i++) {
            if (mPowerUpCards[i] == card) {
                mPowerUpCards[i] = null;
                notify(new PlayerPowerUpsUpdate(mColor, mPowerUpCards));
                return ;
            }
        }
    }

    /**
     * Discard selected powerUp
     * @param powerUpIndex PowerUp index
     */
    public void discard (int powerUpIndex) {
        mPowerUpCards[powerUpIndex] = null;
        notify(new PlayerPowerUpsUpdate(mColor, mPowerUpCards));
    }

    /**
     * set player on a position
     * @param value the position where to put player
     */
    public void move(Position value) {
        mPos = value;

        notify(new PlayerPositionUpdate(mColor, mPos));
    }

    /**
     * respawn the player
     * @param value the position where to put player
     */
    public void respawn(Position value) {
        // avoid incrementing deaths in first spawn
        if (mDead) {
            incrementDeaths();
        }

        for (int i = 0; i < mDamageTaken.length; i++) {
            mDamageTaken[i] = null;
        }

        notify(new PlayerRespawnUpdate(mColor));
        setDeadStatus();
        move(value);
    }

    /**
     * combine sufferedDamage,sufferedMarks,setDeadStatus methods
     * @param damage damage to add to the current damage of the player
     * @param shooterColor the attacking player
     */
    public void onDamageTaken (Damage damage, PlayerColor shooterColor) {
        sufferedDamage(shooterColor, damage.getDamage());
        sufferedMarks(shooterColor, damage.getMarksNum());
        setDeadStatus();
    }

    /**
     * Reload selected weapon
     * @param weaponIndex Weapon index
     */
    public void reloadWeapon (int weaponIndex) {
        Weapon weapon = getWeapon(weaponIndex);

        payAmmo(weapon.getReloadCost());
        weapon.setLoaded(true);

        notify(new PlayerWeaponsUpdate(mColor, mWeapons));
    }

    /**
     * Wrapper method for unloading weapon and notify it to views
     * @param weaponIndex Weapon index
     */
    public void unloadWeaponForShooting (int weaponIndex) {
        Weapon weapon = getWeapon(weaponIndex);
        weapon.setLoaded(false);

        notify(new PlayerWeaponsUpdate(mColor, mWeapons));
    }

    /**
     * Returns all the indices for the powerups of the given type
     * @param wantedType the wanted type
     * @return all the indices corresponding to the wanted powerup type
     */
    public Set<Integer> getPowerUpIndices(PowerUpType wantedType) {
        return IntStream.range(0, getPowerUps().length)
                .mapToObj(i -> new Pair<>(i, getPowerUps()[i]))
                .filter(pair -> pair.getSecond() != null)
                .filter(pair -> pair.getSecond().getType().equals(wantedType))
                .map(Pair::getFirst)
                .collect(Collectors.toSet());
    }

    /**
     * Returns all the indices for the powerups that this player has
     * @return all the wanted indices
     */
    public Set<Integer> getAllPowerUpIndices() {
        return IntStream.range(0, getPowerUps().length)
                .mapToObj(i -> new Pair<>(i, getPowerUps()[i]))
                .filter(pair -> pair.getSecond() != null)
                .map(Pair::getFirst)
                .collect(Collectors.toSet());
    }

    /**
     * Counts the powerups in the player's hand
     * @return the number of powerups in a {@code this}'s hand
     */
    public long getNumOfPowerupsInHand() {
        return IntStream.range(0, getPowerUps().length)
                .filter(Objects::nonNull)
                .count();
    }
}
