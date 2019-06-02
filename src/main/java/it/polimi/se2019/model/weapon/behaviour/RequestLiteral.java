package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.response.WeaponResponse;
import it.polimi.se2019.view.request.Request;

public class RequestLiteral extends Literal<Request> {
    public RequestLiteral(Request contents) {
        super(contents);
    }

    @Override
    public Request asRequest() {
        return getPrimitive();
    }
}
