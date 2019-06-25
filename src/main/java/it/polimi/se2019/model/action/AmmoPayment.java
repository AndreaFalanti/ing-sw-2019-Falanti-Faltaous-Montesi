package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PowerUpCard;

public final class AmmoPayment {
    // private constructor, avoids class instantiation
    private AmmoPayment() {
    }

    /**
     * Handle ammo payment interaction
     * @param player Player that is paying an ammo cost
     * @param cost Total cost to pay
     * @param discardedCards Array of discarded powerUps
     */
    public static void payCost (Player player, AmmoValue cost, boolean[] discardedCards) {
        if (player == null || cost == null || discardedCards == null) {
            throw new IllegalArgumentException("null values in method payCost");
        }

        addAmmoAndDiscard(player, discardedCards);
        player.payAmmo(cost);
    }

    /**
     * Check that ammo payment is valid
     * @param player Player that need to pay a cost
     * @param cost Total cost to pay
     * @param discardedCards Array of discarded powerUps
     * @return true if can pay, false otherwise
     */
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
     * Get total ammo considering also power up cards discarded
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

    /**
     * Discard selected powerUps and give ammo to player
     * @param player Player that discard powerUps
     * @param discardedCards Array of discarded powerUps
     */
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

    /**
     * Check if player can pay an additional ammo cost not covered by his actual ammo, discarding the
     * powerUps in his hand.
     * @param player Player that pay the cost
     * @param remainingCost Remaining cost to pay
     * @return True if can pay, false otherwise
     */
    public static boolean canPayWithPowerUps (Player player, AmmoValue remainingCost) {
        AmmoValue ammoValue = new AmmoValue(0,0,0);
        PowerUpCard[] powerUpCards = player.getPowerUps();

        for (int i = 0; i < powerUpCards.length; i++) {
            if (powerUpCards[i] != null) {
                ammoValue.add(powerUpCards[i].getAmmoValue());
            }
        }

        return ammoValue.isBiggerOrEqual(remainingCost);
    }
}
