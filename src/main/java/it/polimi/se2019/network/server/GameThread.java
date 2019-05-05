package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.view.View;

public class GameThread implements Runnable{
    private Game model;
    private Controller controller;
    private View[] virtualViews;

    public GameThread(){
   //     this.model = model;
        //TODO: needed parameters

    }

    @Override
    public void run(){
        //TODO implementation

    }

 //   public void startThreads(){
 //       GameThread game1 = new GameThread();
 //       game1.run();
 //   }
}
