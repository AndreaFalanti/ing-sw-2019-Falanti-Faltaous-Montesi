package it.polimi.se2019.model;

import it.polimi.se2019.model.board.TileColor;


public class PowerUpCard {
    private String mName;
    private AmmoValue mAmmoValue;
    private PowerUpBehaviour mBehaviour;

    /**
     *
     * @param name Card name
     * @param ammo Ammo value of the card
     * @param behaviour Card effect
     * @throws IllegalArgumentException Thrown if AmmoValue total isn't of 1 ammo cube
     */
    public PowerUpCard (String name, AmmoValue ammo, PowerUpBehaviour behaviour) {
        if (ammo.getRed() + ammo.getYellow() + ammo.getBlue() != 1) {
            throw new IllegalArgumentException ();
        }

        mName = name;
        mAmmoValue = ammo;
        mBehaviour = behaviour;
    }

    /**
     * Get card color from its AmmoValue
     * @return card color
     */
    public TileColor getColor() {
        if (mAmmoValue.getRed() == 1) {
            return TileColor.RED;
        }
        else if (mAmmoValue.getYellow() == 1) {
            return TileColor.YELLOW;
        }
        else {
            return TileColor.BLUE;
        }
    }

    public String getName() {
        return mName;
    }

    /**
     * Activate card effect
     * @param player Target player for the effect processing
     */
    public void activate(Player player) {
        mBehaviour.activate(player);
    }

    @Override
    public String toString() {
        return "PowerUpCard{" +
                "mName='" + mName + '\'' +
                ", mAmmoValue=" + mAmmoValue +
                ", mBehaviour=" + mBehaviour +
                '}';
    }
}
