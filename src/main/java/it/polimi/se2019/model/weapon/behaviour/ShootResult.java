package it.polimi.se2019.model.weapon.behaviour;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.action.Action;

import java.util.Objects;
import java.util.Optional;

public class ShootResult {
    // these two will never be present at the same time
    private Optional<Action> mActionValue;
    private Optional<Response> mResponseValue;

    private ShootResult() {}
    private ShootResult(Action action) {
        mResponseValue = Optional.empty();
        mActionValue = Optional.of(action);
    }
    private ShootResult(Response request) {
        mResponseValue = Optional.of(request);
        mActionValue = Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShootResult casted = (ShootResult) o;

        return toJsonTree().equals(casted.toJsonTree());
    }

    @Override
    public int hashCode() {
        return Objects.hash(toJsonTree());
    }

    @Override
    public String toString() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(toJsonTree());
    }

    public JsonElement toJsonTree() {
        return new Gson().toJsonTree(this, ShootResult.class);
    }

    public static ShootResult from(Action action) {
        return new ShootResult(action);
    }

    public static ShootResult from(Response response) {
        return new ShootResult(response);
    }

    public boolean isComplete() {
        if (mResponseValue.isPresent() && mActionValue.isPresent())
            throw new IllegalStateException("Shoot request is both complete and not...");

        return mActionValue.isPresent();
    }

    public Action asAction() {
        if (!isComplete())
            throw new UnsupportedOperationException("Cannot interpret incomplete shoot result as an action!");

        return mActionValue.isPresent() ? mActionValue.get() : null;
    }

    public Response asResponse() {
         if (isComplete())
            throw new UnsupportedOperationException("Cannot interpret complete shoot result as a response!");

        return mResponseValue.isPresent() ? mResponseValue.get() : null;
    }
}
