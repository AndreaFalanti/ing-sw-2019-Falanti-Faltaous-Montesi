package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.request.Request;

public class RequestLiteral extends Literal<Request> {
    public RequestLiteral(Request contents) {
        super(contents);
    }

    @Override
    Request asRequest() {
        return getPrimitive();
    }
}
