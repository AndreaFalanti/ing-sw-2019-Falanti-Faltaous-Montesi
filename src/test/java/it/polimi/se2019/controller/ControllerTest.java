package it.polimi.se2019.controller;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.GrabAmmoAction;
import it.polimi.se2019.model.action.MoveGrabAction;
import it.polimi.se2019.util.GameTestCaseBuilder;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.ActionRequest;
import it.polimi.se2019.view.request.WeaponSelectedRequest;
import org.junit.Test;

import java.util.EnumMap;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ControllerTest {
    @Test
    public void testGrabActionStrategyCompletable () {
        Game game = GameTestCaseBuilder.generateBaseGame();
        View viewMock = mock(View.class);
        EnumMap<PlayerColor, View> mViewMap = new EnumMap<>(PlayerColor.class);

        mViewMap.put(PlayerColor.BLUE, viewMock);
        Controller controller = new Controller(game, mViewMap);

        // move to spawn
        game.getActivePlayer().move(new Position(2, 0));
        // fill ammo
        game.getActivePlayer().addAmmo(new AmmoValue(3,3,3));
        controller.handle(new ActionRequest(new GrabAmmoAction(), PlayerColor.BLUE));

        controller.handle(new WeaponSelectedRequest(1, PlayerColor.BLUE));
        assertNotNull(game.getActivePlayer().getWeapon(0));

        System.out.println(game.getActivePlayer().getWeapon(0));

        // move out of spawn
        game.getActivePlayer().move(new Position(1, 0));
        controller.handle(new ActionRequest(new MoveGrabAction(PlayerColor.BLUE, new Position(2, 0)), PlayerColor.BLUE));

        controller.handle(new WeaponSelectedRequest(1, PlayerColor.BLUE));
        assertNotNull(game.getActivePlayer().getWeapon(1));

        System.out.println(game.getActivePlayer().getWeapon(1));
    }
}