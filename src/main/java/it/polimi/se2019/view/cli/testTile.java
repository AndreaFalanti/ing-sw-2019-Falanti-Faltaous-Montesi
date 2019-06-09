package it.polimi.se2019.view.cli;

public class testTile {
    private StringBuilder color;
    private boolean[] doors = new boolean[4];
    public testTile(StringBuilder color,boolean door0,boolean door1,boolean door2,boolean door3){
        this.color = color;
        doors[0] = door0;
        doors[1] = door1;
        doors[2] = door2;
        doors[3] = door3;
    }

    public StringBuilder getColor(){return color;}
    public boolean[] getDoors(){return doors;}
}
