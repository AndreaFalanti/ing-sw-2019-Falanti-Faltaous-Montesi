package it.polimi.se2019.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class AmmoCard {
    private AmmoValue mAmmoGain;
    private boolean mDrawPowerUp;

    public AmmoCard() { }

    /**
     *
     * @param ammo Ammo given when the card is grabbed
     * @param drawPowerUp Set if player can draw a PowerUpCard on grab
     */
    public AmmoCard(AmmoValue ammo, boolean drawPowerUp) {
        if (ammo == null) {
            throw new IllegalArgumentException("AmmoValue can't be null");
        }

        mAmmoGain = ammo;
        mDrawPowerUp = drawPowerUp;
    }

    public AmmoValue getAmmoGain () {
        return mAmmoGain;
    }

    public boolean getDrawPowerUp() {
        return mDrawPowerUp;
    }

    public AmmoCard deepCopy() {
        AmmoCard result = new AmmoCard();

        result.mAmmoGain = mAmmoGain.deepCopy();
        result.mDrawPowerUp = mDrawPowerUp;

        return result;
    }

    /**
     * Return a complete list of AmmoCards parsed from a json.
     * @param json Json to parse
     * @return List of AmmoCards
     */
    public static List<AmmoCard> returnDeckFromJson (String json) {
        Gson gson = new Gson();
        AmmoCardStruct[] ammoCardStructs = gson.fromJson(json, AmmoCardStruct[].class);
        ArrayList<AmmoCard> cards = new ArrayList<>();
        for (AmmoCardStruct struct : ammoCardStructs) {
            for (int i = 0; i < struct.quantity; i++) {
                cards.add(struct.card);
            }
        }
        return cards;
    }

    /**
     * Helper class for AmmoCards deserialization.
     */
    private class AmmoCardStruct {
        AmmoCard card;
        int quantity;
    }

    @Override
    public String toString() {
        return "AmmoCard{" +
                "mAmmoGain=" + mAmmoGain +
                ", mDrawPowerUp=" + mDrawPowerUp +
                '}';
    }
}
