package it.polimi.se2019.model;

import com.google.gson.Gson;
import it.polimi.se2019.model.board.TileColor;

import java.util.ArrayList;
import java.util.List;


public class PowerUpCard {
    private PowerUpType mType;
    private AmmoValue mAmmoValue;
    private String mGuiID;

    /**
     *
     * @param type Card type
     * @param ammo Ammo value of the card
     * @throws IllegalArgumentException Thrown if AmmoValue total isn't of 1 ammo cube
     */
    public PowerUpCard(PowerUpType type, AmmoValue ammo) {
        if (ammo != null && ammo.getRed() + ammo.getYellow() + ammo.getBlue() != 1) {
            throw new IllegalArgumentException ();
        }

        mType = type;
        mAmmoValue = ammo;
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

    public PowerUpType getType() {
        return mType;
    }

    public AmmoValue getAmmoValue() {
        return mAmmoValue;
    }

    public String getGuiID() {
        return mGuiID;
    }


    /**
     * Return a complete list of PowerUpCards parsed from a json.
     * @param json Json to parse
     * @return List of PowerUpCards
     */
    public static List<PowerUpCard> returnDeckFromJson (String json) {
        Gson gson = new Gson();
        PowerUpStruct[] powerUpStructs = gson.fromJson(json, PowerUpStruct[].class);
        ArrayList<PowerUpCard> cards = new ArrayList<>();
        for (PowerUpStruct struct : powerUpStructs) {
            for (int i = 0; i < struct.quantity; i++) {
                cards.add(struct.card);
            }
        }

        return cards;
    }

    /**
     * Helper class for PowerUpCard deserialization.
     */
    private class PowerUpStruct {
        PowerUpCard card;
        int quantity;
    }

    @Override
    public String toString() {
        return "PowerUpCard{" +
                "mType='" + mType + '\'' +
                ", mAmmoValue=" + mAmmoValue +
                ", mGuiID='" + mGuiID + '\'' +
                '}';
    }
}
