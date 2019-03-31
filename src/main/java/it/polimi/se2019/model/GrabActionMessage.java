package it.polimi.se2019.model;

import java.util.*;


public abstract class GrabActionMessage extends ActionMessage {

    public abstract void perform(Model model);

    public abstract boolean isValid(Model model);

}
