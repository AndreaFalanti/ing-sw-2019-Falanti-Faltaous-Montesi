package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.DamageAction;
import it.polimi.se2019.model.action.WeaponAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class InflictDamageTest {
    /****************************************************************************/
    /* ShootContext mMarioShootsLuigi;                                          */
    /*                                                                          */
    /* @Before                                                                  */
    /* public void instantiate() {                                              */
    /*     mMarioShootsLuigi = new ShootContext(                                */
    /*             Board.fromJson(Jsons.get("boards/tests/simple_board")),      */
    /*             new HashSet(Arrays.asList(                                   */
    /*                     new Player("Mario", PlayerColor.PURPLE),             */
    /*                     new Player("Luigi", PlayerColor.GREEN)               */
    /*             )),                                                          */
    /*             PlayerColor.PURPLE                                           */
    /*     );                                                                   */
    /* }                                                                        */
    /*                                                                          */
    /* @Test                                                                    */
    /* public void testEval() {                                                 */
    /*     // inflict 1 damage to Mario                                         */
    /*     InflictDamage tested = new InflictDamage(                            */
    /*             new DamageLiteral(new Damage(1, 0)),                         */
    /*             new TargetsLiteral(Collections.singleton(PlayerColor.GREEN)) */
    /*     );                                                                   */
    /*                                                                          */
    /*     // check                                                             */
    /*     AtomicExpression expected = new Done();                              */
    /*     AtomicExpression actual = tested.eval(mMarioShootsLuigi);            */
    /*     assertEquals(expected, actual);                                      */
    /*                                                                          */
    /*     // check side effects on context (resulting action is stored there)  */
    /*     Action actualAction = mMarioShootsLuigi.getResultingAction();        */
    /*     Action expectedAction = new WeaponAction(                            */
    /*             new DamageAction(                                            */
    /*                     PlayerColor.PURPLE,                                  */
    /*                     Collections.singleton(PlayerColor.GREEN),            */
    /*                     new Damage(1, 0)                                     */
    /*             )                                                            */
    /*     );                                                                   */
    /*     assertEquals(expectedAction, actualAction);                          */
    /* }                                                                        */
    /*                                                                          */
    /* @Test                                                                    */
    /* public void testEvalToShootResult() {                                    */
    /*      // inflict 1 damage to Mario                                        */
    /*     InflictDamage tested = new InflictDamage(                            */
    /*             new DamageLiteral(new Damage(1, 0)),                         */
    /*             new TargetsLiteral(Collections.singleton(PlayerColor.GREEN)) */
    /*     );                                                                   */
    /*                                                                          */
    /*     // check it                                                          */
    /*     ShootResult expected = ShootResult.from(new WeaponAction(            */
    /*             new DamageAction(                                            */
    /*                     PlayerColor.PURPLE,                                  */
    /*                     Collections.singleton(PlayerColor.GREEN),            */
    /*                     new Damage(1, 0)                                     */
    /*             )                                                            */
    /*     ));                                                                  */
    /*     ShootResult actual = tested.evalToShootResult(mMarioShootsLuigi);    */
    /*     assertEquals(expected, actual);                                      */
    /* }                                                                        */
    /****************************************************************************/
}
