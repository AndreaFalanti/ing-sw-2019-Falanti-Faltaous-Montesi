package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionTest {
    private Expression makeDummyExpression() {
        return new InflictDamage(
                new DamageLiteral(new Damage(1, 2)),
                new NegateSelection(
                        new SetExpression(
                                new TargetLiteral(PlayerColor.GREEN),
                                new TargetLiteral(PlayerColor.YELLOW)
                        )
                )
        );
    }

    @Test
    public void testDeepCopy() {
        Expression original = makeDummyExpression();

        assertEquals(makeDummyExpression(), original.deepCopy());
    }
}
