package it.polimi.se2019.util;

import java.util.ArrayList;
import java.util.List;

public class Observable<Message> {
    private final List<Observer<Message>> mObservers = new ArrayList<>();

    public void register(Observer<Message> toRegister) {
        mObservers.add(toRegister);
    }

    protected void notify(Message message) {
        mObservers.forEach(observer -> observer.update(message));
    }
}