package it.polimi.se2019.controller.response;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.view.ResponseHandler;

/**
 * Response used to notify view of a needed weapon mode selection
 *
 * @author Andrea Falanti
 */
public class PickWeaponModeResponse implements Response {
    private Effect mEffect1;
    private Effect mEffect2;

    public PickWeaponModeResponse(Effect effect1, Effect effect2) {
        mEffect1 = effect1;
        mEffect2 = effect2;
    }

    public Effect getEffect1() {
        return mEffect1;
    }

    public Effect getEffect2() {
        return mEffect2;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
