package com.gmail.zubkovskiy.trainingdiary.model;


import java.util.Comparator;
import java.util.List;

public class Exercise  {

    public static final String CATEGORY_ARMS = "arms";
    public static final String CATEGORY_LEGS = "legs";
    public static final String CATEGORY_BACK = "back";
    public static final String CATEGORY_SHOULDERS = "shoulders";
    public static final String CATEGORY_CHEST = "chest";

    private String title;

    private transient String description;

    private String category;

    private List<Approach> approaches;

    private int orderInTraining;

    public Exercise(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }
    public Exercise(Exercise exercise){
        this.title = exercise.getTitle();
        this.category = exercise.getCategory();
        this.description = exercise.getDescription();
    }

    public List<Approach> getApproaches() {
        return approaches;
    }

    public void setApproaches(List<Approach> approaches) {
        this.approaches = approaches;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getOrderInTraining() {
        return orderInTraining;
    }

    public void setOrderInTraining(int orderInTraining) {
        this.orderInTraining = orderInTraining;
    }

    @Override
    public boolean equals(Object o) {
        Exercise exercise = (Exercise) o;
        if (this.title.equalsIgnoreCase(exercise.getTitle()) && this.description.equalsIgnoreCase(exercise.getDescription()))
            return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }


    public static Comparator<Exercise> categoryComparator = new Comparator<Exercise>() {
        @Override
        public int compare(Exercise lhs, Exercise rhs) {
            return lhs.getCategory().compareToIgnoreCase(rhs.getCategory());
        }
    };

    public static Comparator<Exercise> titleComparator = new Comparator<Exercise>() {
        @Override
        public int compare(Exercise lhs, Exercise rhs) {
            return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
        }

    };

    public static Comparator<Exercise> orderComparator = new Comparator<Exercise>() {
        @Override
        public int compare(Exercise lhs, Exercise rhs) {
            if(lhs.orderInTraining > rhs.orderInTraining)
                return 1;
            else if(lhs.orderInTraining == rhs.orderInTraining)
                return 0;
            else return -1;
        }
    };
}
