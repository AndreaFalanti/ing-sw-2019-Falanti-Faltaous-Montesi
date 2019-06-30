package it.polimi.se2019.util;

import java.util.ArrayList;
import java.util.List;

public class Observable<Message> {
    private final List<Observer<Message>> mObservers = new ArrayList<>();

    public void register(Observer<Message> toRegister) {
        mObservers.add(toRegister);
    }
    public void unregister(Observer<Message> toUnregister) {
        mObservers.remove(toUnregister);
    }

    public List<Observer<Message>> getObservers() {
        return mObservers;
    }

    public void notify(Message message) {//TODO change in protect
        mObservers.stream()
                .peek(observer -> System.out.println(getClass().getSimpleName() + " is notifying " + observer.getClass().getSimpleName() + ": " + message))
                .forEach(observer -> observer.update(message));
    }
}