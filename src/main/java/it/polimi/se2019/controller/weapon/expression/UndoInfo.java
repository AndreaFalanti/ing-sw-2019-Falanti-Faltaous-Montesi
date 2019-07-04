package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class recording information to undo a game state
 */
public class UndoInfo {
    private static class PlayerUndoInfo {
        private Position mOriginalPosition;
        private AmmoValue mOriginalAmmo;
        private PlayerColor[] mOriginalDamageTaken;
        private Map<PlayerColor, Integer> mOriginalMarks;
        private boolean mOriginalDead;
        private Weapon[] mOriginalWeapons;
        private PowerUpCard[] mOriginalPowerUpCards;

        PlayerUndoInfo(Player player) {
            if (player == null || player.getWeapons() == null)
                throw new NullPointerException();

            mOriginalPosition = player.getPos() == null ?
                    null :
                    player.getPos().deepCopy();
            mOriginalAmmo = player.getAmmo().deepCopy();
            mOriginalDamageTaken = player.getDamageTaken().clone();
            mOriginalMarks = player.getMarks().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                    ));
            mOriginalDead = player.isDead();

            int weaponsNum = player.getWeapons().length;
            mOriginalWeapons = new Weapon[weaponsNum];
            for (int i = 0; i < weaponsNum; i++) {
                Weapon weapon =  player.getWeapon(i);
                mOriginalWeapons[i] = (weapon != null) ? weapon.deepCopy() : null;
            }

            int powerUpNum = player.getPowerUps().length;
            mOriginalPowerUpCards = new PowerUpCard[powerUpNum];
            for (int i = 0; i < powerUpNum; i++) {
                mOriginalPowerUpCards[i] = player.getPowerUpCard(i);
            }
        }

        void undoPlayer(Player player, boolean activePlayer) {
            player.move(mOriginalPosition);
            player.setMarks(mOriginalMarks);
            player.setPowerUpCards(mOriginalPowerUpCards);

            // only for shooter
            if (activePlayer) {
                player.setAmmo(mOriginalAmmo);
                player.setWeapons(mOriginalWeapons);
            }
            // only for other players
            else {
                player.setDamageTaken(mOriginalDamageTaken);
                player.setDead(mOriginalDead);
            }
        }
    }

    private PlayerColor mActivePlayerColor;
    private Game mGame;
    private Map<PlayerColor, PlayerUndoInfo> mPlayerUndoInfo;
    private boolean mUndone = false;

    public UndoInfo(PlayerColor activePlayerColor, Game game) {
        mActivePlayerColor = activePlayerColor;
        mGame = game;
        mPlayerUndoInfo = mGame.getPlayers().stream()
                .collect(Collectors.toMap(
                        Player::getColor,
                        PlayerUndoInfo::new
                ));
    }

    /**
     * Undoes the side effects that the evaluation of an expression had on a game object
     */
    public void doUndo() {
        if (mUndone)
            throw new IllegalStateException("Undo has already been done!");
        mUndone = true;

        mGame.getPlayers()
                .forEach(pl -> mPlayerUndoInfo.get(pl.getColor())
                        .undoPlayer(pl, pl.getColor().equals(mActivePlayerColor)));
        mGame.increaseActionCounter();
    }
}
