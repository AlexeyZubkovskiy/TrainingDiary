package com.gmail.zubkovskiy.trainingdiary.model;


import java.util.Comparator;

public class Approach {

    public static Comparator<Approach> sortApproaches = new Comparator<Approach>() {
        @Override
        public int compare(Approach lhs, Approach rhs) {
            if (lhs.number > rhs.number)
                return 1;
            else if (lhs.number == rhs.number)
                return 0;
            else return -1;
        }
    };

    private int number;

    private float weight;

    private int repeats;

    public Approach(int number) {
        this.number = number;
    }

   /* public Approach(int number, float weight) {
        this.number = number;
        this.weight = weight;
    }

    public Approach(int number, float weight, int repeats) {

        this.number = number;
        this.weight = weight;
        this.repeats = repeats;
    }*/

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getWeight() {
        return weight;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }




}
