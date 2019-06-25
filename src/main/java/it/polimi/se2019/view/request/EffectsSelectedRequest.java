package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

import java.util.List;

public class EffectsSelectedRequest implements Request {
    private List<String> mEffects;
    private PlayerColor mViewColor;

    public EffectsSelectedRequest(List<String> effects, PlayerColor viewColor) {
        mEffects = effects;
        mViewColor = viewColor;
    }

    public List<String> getSelectedEffects() {
        return mEffects;
    }

    @Override
    public PlayerColor getViewColor() {
        return mViewColor;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
