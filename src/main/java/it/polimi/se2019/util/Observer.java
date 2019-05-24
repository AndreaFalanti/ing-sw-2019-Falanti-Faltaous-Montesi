package it.polimi.se2019.util;

public interface Observer<Message> {
    void update(Message message);
}