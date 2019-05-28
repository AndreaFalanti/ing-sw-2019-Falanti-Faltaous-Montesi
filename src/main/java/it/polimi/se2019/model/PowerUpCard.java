package it.polimi.se2019.model;

import com.google.gson.Gson;
import it.polimi.se2019.model.board.TileColor;

import java.util.ArrayList;
import java.util.List;


public class PowerUpCard {
    private String mName;
    private AmmoValue mAmmoValue;
    private PowerUpBehaviour mBehaviour;
    private String mGuiID;

    /**
     *
     * @param name Card name
     * @param ammo Ammo value of the card
     * @param behaviour Card effect
     * @throws IllegalArgumentException Thrown if AmmoValue total isn't of 1 ammo cube
     */
    public PowerUpCard (String name, AmmoValue ammo, PowerUpBehaviour behaviour) {
        if (ammo != null && ammo.getRed() + ammo.getYellow() + ammo.getBlue() != 1) {
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

    public AmmoValue getAmmoValue() {
        return mAmmoValue;
    }

    public PowerUpBehaviour getBehaviour() {
        return mBehaviour;
    }

    public String getGuiID() {
        return mGuiID;
    }

    /**
     * Activate card effect
     * @param player Target player for the effect processing
     */
    public void activate(Player player) {
        mBehaviour.activate(player);
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
            switch (struct.card.mName) {
                case "Targeting scope":
                    struct.card.mBehaviour = new TargetingScopeBehaviour();
                    break;
                case "Teleport":
                    struct.card.mBehaviour = new TeleportBehaviour();
                    break;
                case "Tagback grenade":
                    struct.card.mBehaviour = new TagbackGrenadeBehaviour();
                    break;
                case "Newton":
                    struct.card.mBehaviour = new NewtonBehaviour();
                    break;
                default:
                    throw new IllegalArgumentException("PowerUp name not recognized, can't set proper behaviour");
            }

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
                "mName='" + mName + '\'' +
                ", mAmmoValue=" + mAmmoValue +
                ", mBehaviour=" + mBehaviour +
                ", mGuiID='" + mGuiID + '\'' +
                '}';
    }
}
