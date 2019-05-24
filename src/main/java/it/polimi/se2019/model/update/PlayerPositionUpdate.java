package it.polimi.se2019.model.update;

public class PlayerPositionUpdate implements Update {
    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
