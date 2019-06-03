package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Player;

public final class AmmoPayment {
    private AmmoPayment() {
    }

    public static void payCost (Player player, AmmoValue cost, boolean[] discardedCards) {
        if (player == null || cost == null || discardedCards == null) {
            throw new IllegalArgumentException("null values in method payCost");
        }

        addAmmoAndDiscard(player, discardedCards);
        player.payAmmo(cost);
    }

    public static boolean isValid (Player player, AmmoValue cost, boolean[] discardedCards) {
        if (player == null || cost == null || discardedCards == null) {
            throw new IllegalArgumentException("null values in method getErrorResponse");
        }

        AmmoValue playerAmmo = player.getAmmo();
        boolean isDiscarding = false;

        // check that user is trying to discard a valid card (not null)
        for (int i = 0; i < discardedCards.length; i++) {
            if (discardedCards[i]) {
                isDiscarding = true;
                if (player.getPowerUpCard(i) == null) {
                    return false;
                }
            }
        }

        // check that if player can pay the cost directly, it's not discarding power up cards
        if (playerAmmo.isBiggerOrEqual(cost)) {
            return !isDiscarding;
        }
        else {
            AmmoValue ammoWithPowerUps = getAmmoTotalWithPowerUpDiscard(player, discardedCards);
            return ammoWithPowerUps.isBiggerOrEqual(cost);
        }
    }

    /**
     * Get total ammo considering also power up card discarded
     * @param player Player that is performing the reload
     * @param discardedCards Boolean array that indicate discarded cards
     * @return Total ammo with power up bonus ones
     */
    private static AmmoValue getAmmoTotalWithPowerUpDiscard (Player player, boolean[] discardedCards) {
        AmmoValue ammo = player.getAmmo().deepCopy();
        for (int i = 0; i < discardedCards.length; i++) {
            if (discardedCards[i]) {
                ammo.add(player.getPowerUpCard(i).getAmmoValue());
            }
        }

        return ammo;
    }

    public static void addAmmoAndDiscard (Player player, boolean[] discardedCards) {
        if (player == null || discardedCards == null) {
            throw new IllegalArgumentException("null values in method addAmmoAndDiscard");
        }

        // discard powerUps set in boolean array and add ammo to player
        for (int i = 0; i < discardedCards.length; i++) {
            if (discardedCards[i]) {
                player.getAmmo().add(player.getPowerUpCard(i).getAmmoValue());
                player.discard(i);
            }
        }
    }
}
