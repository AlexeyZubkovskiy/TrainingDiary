package com.gmail.zubkovskiy.trainingdiary.model;


import com.gmail.zubkovskiy.trainingdiary.utils.Logf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Training {

    public static SimpleDateFormat trainingDateFormat = new SimpleDateFormat("cccc dd.MMM.yyyy kk:mm",Locale.getDefault());

    private String name;
    private ArrayList<Exercise> exercises;
    private String date;

    public Training(){

    }

    public Training(String name){
        this.name = name;
        this.exercises = new ArrayList<>();
        this.date = trainingDateFormat.format(new Date());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addExercise(Exercise exercise){
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise){
        exercises.remove(exercise);
    }

    public void addExercises(ArrayList<Exercise> exercises){
        this.exercises.addAll(exercises);
    }

    public void removeExercises(ArrayList<Exercise> exercises){

        this.exercises.removeAll(exercises);
    }

    public List<Exercise> getExercises(){
        return this.exercises;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public static Comparator<Training> trainingDateComparator = new Comparator<Training>() {
        @Override
        public int compare(Training lhs, Training rhs) {
            Date date1 ;
            Date date2 ;
            try {
                date1 = Training.trainingDateFormat.parse(lhs.getDate());
                date2 = Training.trainingDateFormat.parse(rhs.getDate());
            } catch (ParseException e) {
                Logf.e("Trainings.trainingDateComparator", e.getMessage());
                //This initialization is hack
                date1 = new Date();
                date2 = new Date();
            }
            if (date1 != null && date2 != null)
                return date1.compareTo(date2);
            else
                return 0;
        }
    };
}
