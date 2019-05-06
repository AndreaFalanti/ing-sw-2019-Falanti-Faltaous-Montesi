package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.weapon.behaviour.*;

public class Weapons {
    // TODO: add doc
    private Weapons() {}

    // TODO: add doc
    // TODO: expand behaviour to make impl. possible
    static Weapon makeLockRifle() {
        // Expression primary = new InflictDamage(
                // new Damage(2, 1),
                // new CanSee()
        // );

        // Expression with_second_lock =
        return null;
    }

    // TODO: add doc
    static Weapon makeWhisper() {
        Expression primary = new InflictDamage(
                new DamageLiteral(new Damage(3, 1)),
                new SelectTargets(new IntLiteral(1), new IntLiteral(1), new CanSee()) // TODO: add "two moves away from you"
        );

        /*
        primary = Expr.inflictDamage(
                Expr.dam(3, 1),
                Expr.selectOneTarget(
                        Expr.intersetRanges(
                                Expr.distAtLeast(2),
                                Expr.canSee()
                        )
                )
        );
         */

        return new Weapon(primary);
    }

    // TODO: add doc
    static Weapon fromID(WeaponID id) {
        if (id == WeaponID.LOCK_RIFLE) {
            return makeLockRifle();
        } else if (id == WeaponID.WHISPER) {
            return makeWhisper();
        }
        throw new IllegalArgumentException(id + " not existent or not implemented yet");
    }
}
