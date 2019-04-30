package it.polimi.se2019.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.view.View;

public class GameThread extends Thread{
    private Game model;
    private Controller controller;
    private View[] virtualViews;


    @Override
    public void run(){


    }

    public void startThreads(){
        GameThread game1 = new GameThread();
        game1.start();
    }
}
