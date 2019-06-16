package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.view.View;

import java.util.List;

public class EffectsSelectedRequest implements Request {
    private List<String> mEffects;
    private View mView;

    public EffectsSelectedRequest(List<String> effects, View view) {
        mEffects = effects;
        mView = view;
    }

    public List<String> getEffects() {
        return mEffects;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
