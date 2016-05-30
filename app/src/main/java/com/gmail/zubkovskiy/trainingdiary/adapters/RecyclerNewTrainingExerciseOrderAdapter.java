package com.gmail.zubkovskiy.trainingdiary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.events.ClearOrderValues;
import com.gmail.zubkovskiy.trainingdiary.events.StartTraining;
import com.gmail.zubkovskiy.trainingdiary.events.UnableToStartTraining;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;

import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alexey.zubkovskiy@gmail.com on 21.05.2016.
 */
public class RecyclerNewTrainingExerciseOrderAdapter extends RecyclerView.Adapter<RecyclerNewTrainingExerciseOrderAdapter.Holder> {

    private static RecyclerNewTrainingExerciseOrderAdapter mAdapterInstance;
    private List<Exercise> mExercises;
    //private Context mContext;
    private HashMap<Exercise, EditText> orderValues;

    private RecyclerNewTrainingExerciseOrderAdapter(List<Exercise> exercises, Context context) {
        this.mExercises = exercises;
        //this.mContext = context;
        BusProvider.getInstance().register(this);
    }

    public static RecyclerNewTrainingExerciseOrderAdapter getInstance(List<Exercise> exercises, Context context) {
        if (mAdapterInstance == null) {
            mAdapterInstance = new RecyclerNewTrainingExerciseOrderAdapter(exercises, context);
        } else {
            mAdapterInstance.setExerciseBase(exercises);
            //mAdapterInstance.setContext(context);
        }
        //to clear all old values
        if (mAdapterInstance.orderValues != null)
            mAdapterInstance.orderValues.clear();
        else
            mAdapterInstance.orderValues = new HashMap<>();

        return mAdapterInstance;
    }

    protected static class Holder extends RecyclerView.ViewHolder {

        private EditText mOrderInTraining;
        private TextView mExerciseCategory;
        private TextView mExerciseTitle;

        public Holder(View itemView) {
            super(itemView);
            mOrderInTraining = (EditText) itemView.findViewById
                    (R.id.fragment_new_training_exercises_order_pattern_order_et);
            mExerciseCategory = (TextView) itemView.findViewById
                    (R.id.fragment_new_training_exercises_order_pattern_exercise_category_tv);
            mExerciseTitle = (TextView) itemView.findViewById
                    (R.id.fragment_new_training_exercises_order_pattern_exercise_name_tv);


        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_new_training_exercises_order_pattern, parent, false);
        Logf.w("onCreateViewHolder()", "RecyclerNewTrainingExerciseOrderAdapter");
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Exercise exercise = mExercises.get(position);
        String category = exercise.getCategory();
        switch (category) {
            case Exercise.CATEGORY_ARMS:
                holder.mExerciseCategory.setText(R.string.ARMS);
                break;
            case Exercise.CATEGORY_BACK:
                holder.mExerciseCategory.setText(R.string.BACK);
                break;
            case Exercise.CATEGORY_LEGS:
                holder.mExerciseCategory.setText(R.string.LEGS);
                break;
            case Exercise.CATEGORY_SHOULDERS:
                holder.mExerciseCategory.setText(R.string.SHOULDERS);
                break;
            case Exercise.CATEGORY_CHEST:
                holder.mExerciseCategory.setText(R.string.CHEST);
                break;
            default:
                holder.mExerciseCategory.setText(category);
        }
        holder.mExerciseTitle.setText(exercise.getTitle());
        orderValues.put(exercise, holder.mOrderInTraining);


        Logf.w("RNTEOA", "onBindViewHolder() value of orderValues" + orderValues.toString());


    }

    @Override
    public int getItemCount() {
        return mExercises != null ? mExercises.size() : 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        orderValues = new HashMap<>();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        Logf.w("onDetachedFromRecyclerView()", "RecyclerNewTrainingExerciseOrderAdapter");
        super.onDetachedFromRecyclerView(recyclerView);
        orderValues = null;
    }

    @Override
    public void onViewAttachedToWindow(Holder holder) {
        super.onViewAttachedToWindow(holder);

    }

    @Override
    public void onViewDetachedFromWindow(Holder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    protected void finalize() throws Throwable {
        BusProvider.getInstance().unregister(this);
        super.finalize();
    }

    @Subscribe
    public void onClearOrderValues(ClearOrderValues clearOrderValues) {
        Logf.w("onClearOrderValues():", "START METHOD");
        if (clearOrderValues.isClear()) {
            Logf.w("onClearOrderValues():", "clearOrderValues.isClear()" + clearOrderValues.isClear());
            //when clear values first time make Exercise.orderInTraining = 0
            if (orderValues != null) {
                for (Map.Entry<Exercise, EditText> entry : orderValues.entrySet()) {
                    entry.getKey().setOrderInTraining(0);
                    entry.getValue().setText("");
                    Logf.w("onClearOrderValues():", "LOOP:" + entry.getKey().getTitle());
                }
                //clear choosed
                //orderValues.clear();
            }
        }
    }

    @Subscribe
    public void onTrainingStart(StartTraining startTraining) {
        if (startTraining.isStart()) {

            //this iterate iterate map for set number of order to exercise
            for (final Iterator<Map.Entry<Exercise, EditText>> i = orderValues.entrySet().iterator();
                 i.hasNext(); ) {
                final Map.Entry<Exercise, EditText> entry = i.next();
                String temp = entry.getValue().getText().toString();
                //if editText contains number i place it to exercise as number of order
                if (temp.length() > 0 && temp.matches("^\\d+$") && Integer.valueOf(temp) > 0) {
                    entry.getKey().setOrderInTraining(Integer.valueOf(temp));
                } else {
                    BusProvider.getInstance().post(new UnableToStartTraining(true));
                    return;
                }

            }
            //By this code staring new training on NewTrainingExerciseOrderFragment
            BusProvider.getInstance().post(new UnableToStartTraining(false));
        }
       /* for (Exercise e : mExercises) {
            Logf.e("in onTraining start()", "Exercise tittle: " + e.getTitle() + " Exercise number in order: " + e.getOrderInTraining());
        }*/

    }

    /*private void setContext(Context context) {
        this.mContext = context;
    }*/

    private void setExerciseBase(List<Exercise> exercises) {
        mExercises = exercises;
    }
}
