package it.polimi.se2019.model;

import java.util.*;


public class AmmoCard {
    private AmmoValue mAmmoGain;
    private boolean mDrawPowerUp;

    public AmmoCard(AmmoValue ammo, boolean drawPowerUp) {
        mAmmoGain = ammo;
        mDrawPowerUp = drawPowerUp;
    }

    public AmmoValue getAmmoGain () {
        return mAmmoGain;
    }

    public boolean getDrawPowerUp() {
        return mDrawPowerUp;
    }

}
