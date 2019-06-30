package it.polimi.se2019.model.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.util.CustomFieldNamingStrategy;

import java.util.*;
import java.util.stream.Collectors;

// TODO: add doc
// TODO: add implementation
public class WeaponAction implements Action {
    private final List<Action> mActions = new ArrayList();

    public WeaponAction(List<Action> actions) {
        mActions.addAll(actions);
    }

    public WeaponAction(Action... actions) {
       mActions.addAll(Arrays.stream(actions).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        WeaponAction that = (WeaponAction) o;

        return toJsonTree().equals(that.toJsonTree());
    }

    @Override
    public int hashCode() {
        return Objects.hash(toJsonTree());
    }

    @Override
    public String toString() {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

        return prettyGson.toJson(toJsonTree());
    }

    public final JsonElement toJsonTree() {
        Gson gson = new GsonBuilder()
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .create();

        return gson.toJsonTree(this, WeaponAction.class);
    }

    @Override
    public void perform(Game game) {
        mActions.forEach(action -> action.perform(game));
    }

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        return Optional.empty();
    }

    @Override
    public boolean consumeAction() {
        return false;
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    // add action to be executed
    public void add(Action action) {
        if (action == null)
            throw new IllegalArgumentException("Cannot add null action to weapon action!");

        mActions.add(action);
    }
}
