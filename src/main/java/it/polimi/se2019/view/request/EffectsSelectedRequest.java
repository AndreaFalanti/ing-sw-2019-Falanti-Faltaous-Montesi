package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.view.View;

import java.util.Set;

public class EffectsSelectedRequest implements Request {
    private Set<String> mEffects;
    private View mView;

    public EffectsSelectedRequest(Set<String> effects, View view) {
        mEffects = effects;
        mView = view;
    }

    public Set<String> getEffects() {
        return mEffects;
    }

    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
