package com.example.puzzle.event;

public class PieceMoveSuccessEvent {

    private int index;
    public PieceMoveSuccessEvent(int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }
}
