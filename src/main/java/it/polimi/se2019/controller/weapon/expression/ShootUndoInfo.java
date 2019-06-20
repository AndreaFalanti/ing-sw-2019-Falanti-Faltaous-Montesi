package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class recording information to undo a shoot interaction
 */
public class ShootUndoInfo {
    private static class PlayerUndoInfo {
        private Position mOriginalPosition;
        private AmmoValue mOriginalAmmo;
        private PlayerColor[] mOriginalDamageTaken;
        private Map<PlayerColor, Integer> mOriginalMarks;
        private boolean mOriginalDead;
        private int mOriginalDeathsNum;
        private List<Boolean> mOriginalWeaponLoadStatus;

        PlayerUndoInfo(Player player) {
            if (player == null || player.getWeapons() == null)
                throw new NullPointerException();

            mOriginalPosition = player.getPos().deepCopy();
            mOriginalAmmo = player.getAmmo().deepCopy();
            mOriginalDamageTaken = player.getDamageTaken().clone();
            mOriginalMarks = player.getMarks().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                    ));
            mOriginalDead = player.isDead();
            mOriginalDeathsNum = player.getDeathsNum();
            mOriginalWeaponLoadStatus = Arrays.stream(player.getWeapons())
                    .map(weapon -> weapon != null && weapon.isLoaded())
                    .collect(Collectors.toList());
        }

        void undoPlayer(Player player) {
            player.move(mOriginalPosition);
            player.setAmmo(mOriginalAmmo);
            player.setDamageTaken(mOriginalDamageTaken);
            player.setMarks(mOriginalMarks);
            player.setDead(mOriginalDead);
            player.setDeathsNum(mOriginalDeathsNum);
            IntStream.range(0, player.getWeapons().length)
                    .filter(i -> player.getWeapons()[i] != null)
                    .forEach(i ->
                            player.getWeapons()[i].setLoaded(mOriginalWeaponLoadStatus.get(i))
                    );
        }
    }

    private Game mGame;
    private Map<PlayerColor, PlayerUndoInfo> mPlayerUndoInfo;
    private boolean mUndone = false;

    public ShootUndoInfo(Game game) {
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
                .forEach(pl -> mPlayerUndoInfo.get(pl.getColor()).undoPlayer(pl));
    }
}
