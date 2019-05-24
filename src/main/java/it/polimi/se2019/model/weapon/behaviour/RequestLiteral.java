package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.response.WeaponResponse;

public class RequestLiteral extends Literal<WeaponResponse> {
    public RequestLiteral(WeaponResponse contents) {
        super(contents);
    }

    @Override
    WeaponResponse asRequest() {
        return getPrimitive();
    }
}
