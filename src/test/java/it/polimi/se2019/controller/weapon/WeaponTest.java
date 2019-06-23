package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeaponTest {
    Game mSimpleGame;
    private static final int EXPECTED_WEAPONS = 21;

    @Before
    public void instantiate() {
        mSimpleGame = new Game(
                Board.fromJson(Jsons.get("boards/tests/simple_board")),
                Arrays.asList(
                        new Player("Mario", PlayerColor.BLUE),
                        new Player("Giorgio", PlayerColor.GREEN),
                        new Player("Johnny", PlayerColor.GREY)
                ),
                3 // TODO: ask what this is
        );
    }

    @Test
    public void testReturnDeckFromJson() {
        List<Weapon> deck = Weapon.returnDeckFromJson(Jsons.get("WeaponDeck"));

        assertEquals(EXPECTED_WEAPONS, deck.size());
        for (Weapon weapon : deck) {
            assertNotNull(weapon.getName());
            assertNotNull(weapon.getReloadCost());
            assertNotNull(weapon.getGrabCost());
        }
    }
}
