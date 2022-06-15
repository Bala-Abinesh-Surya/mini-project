package com.surya.miniproject.models;

import java.util.ArrayList;

public class Leave{
    // class that holds the attributes for the leaves in a month
    private ArrayList<Integer> leaves;

    // Constructor
    public Leave() {

    }

    // getter and setter methods
    public ArrayList<Integer> getLeaves() {
        return leaves;
    }

    public void setLeaves(ArrayList<Integer> leaves) {
        this.leaves = leaves;
    }
}
