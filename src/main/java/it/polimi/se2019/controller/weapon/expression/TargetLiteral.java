package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.PlayerColor;

import java.util.Collections;
import java.util.Set;

/**
 * A literal containing a target
 * @author Stefano Montesi
 */
public class TargetLiteral extends Literal<PlayerColor> {
    public TargetLiteral(PlayerColor contents) {
        super(contents);
    }

    /**
     * @return the literal as a set of targets
     */
    @Override
    public Set<PlayerColor> asTargets() {
        return Collections.singleton(getPrimitive());
    }

    /**
     * @return the literal as a single target
     */
    @Override
    public PlayerColor asTarget() {
        return getPrimitive();
    }
}
