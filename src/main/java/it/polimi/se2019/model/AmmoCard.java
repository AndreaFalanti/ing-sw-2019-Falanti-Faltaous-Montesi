package it.polimi.se2019.model;

import java.util.*;


public class AmmoCard {

    public AmmoCard() {
    }

    private AmmoValue mAmmoGain;

    private boolean mDrawPowerUp;

    public AmmoCard deepCopy() {
        AmmoCard result = new AmmoCard();

        result.mAmmoGain = mAmmoGain.deepCopy();

        result.mDrawPowerUp = mDrawPowerUp;

        return result;
    }


    public void addAmmo() {
    }

    public boolean getDrawPowerUp() {
        return true;
    }

}
