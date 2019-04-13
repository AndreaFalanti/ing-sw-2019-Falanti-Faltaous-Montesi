package it.polimi.se2019.model;

import com.google.gson.Gson;

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

    public static ArrayList<AmmoCard> returnDeckFromJson (String json) {
        Gson gson = new Gson();
        AmmoCardStruct[] ammoCardStructs = gson.fromJson(json, AmmoCardStruct[].class);
        ArrayList<AmmoCard> cards = new ArrayList<AmmoCard>();
        for (AmmoCardStruct struct : ammoCardStructs) {
            for (int i = 0; i < struct.quantity; i++) {
                cards.add(struct.card);
            }
        }
        return cards;
    }

    private class AmmoCardStruct {
        public AmmoCard card;
        public int quantity;
    }

    @Override
    public String toString() {
        return "AmmoCard{" +
                "mAmmoGain=" + mAmmoGain +
                ", mDrawPowerUp=" + mDrawPowerUp +
                '}';
    }
}
