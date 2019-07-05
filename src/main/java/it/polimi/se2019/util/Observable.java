package it.polimi.se2019.util;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    @Exclude
    private final List<Observer<T>> mObservers = new ArrayList<>();

    public void register(Observer<T> toRegister) {
        mObservers.add(toRegister);
    }
    public void deregister(Observer<T> toUnregister) {
        mObservers.remove(toUnregister);
    }

    public List<Observer<T>> getObservers() {
        return mObservers;
    }

    protected void notify(T message) {

        if (!mObservers.isEmpty())
            mObservers
                    .stream().peek(observer -> System.out.println(getClass().getSimpleName() + " is notifying " + observer.getClass().getSimpleName() + ": " + message))
                    .forEach(observer -> observer.update(message));
    }
}