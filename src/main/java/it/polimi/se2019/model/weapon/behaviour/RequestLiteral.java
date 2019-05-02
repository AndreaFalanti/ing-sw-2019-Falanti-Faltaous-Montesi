package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.request.SelectionRequest;

public class RequestLiteral implements Expression {
    SelectionRequest mContents;

    public RequestLiteral(SelectionRequest contents) {
        mContents = contents;
    }

    @Override
    public Expression eval(Context context) {
        return this;
    }
}
