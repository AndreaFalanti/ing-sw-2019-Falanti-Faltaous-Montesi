package it.polimi.se2019.view.request.weapon;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.weapon.behaviour.Literal;

public class ResponseLiteral extends Literal<Response> {
    public ResponseLiteral(Response contents) {
        super(contents);
    }

    @Override
    public Response asResponse() {
        return getPrimitive();
    }
}
