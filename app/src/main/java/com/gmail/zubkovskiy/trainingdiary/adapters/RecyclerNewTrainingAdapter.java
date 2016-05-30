package com.gmail.zubkovskiy.trainingdiary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.events.ExerciseForTrainingSelected;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.model.Approach;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 14.05.2016.
 */
public class RecyclerNewTrainingAdapter extends RecyclerView.Adapter<RecyclerNewTrainingAdapter.Holder> {

    private static RecyclerNewTrainingAdapter mAdapterInstance;
    protected static List<Exercise> mExerciseBase;
    private Context mContext;


    private RecyclerNewTrainingAdapter(List<Exercise> exercises, Context context) {
        mExerciseBase = exercises;
        this.mContext = context;
    }

    public static RecyclerNewTrainingAdapter getInstance(List<Exercise> exercises, Context context) {
        if (mAdapterInstance == null) {
            mAdapterInstance = new RecyclerNewTrainingAdapter(exercises, context);
        } else {
            mAdapterInstance.setExerciseBase(exercises);
            mAdapterInstance.setContext(context);
        }
        return mAdapterInstance;
    }

    protected static class Holder extends RecyclerView.ViewHolder {

        private TextView mExerciseTitle;
        private TextView mExerciseCategory;
        private Button mChoiseButton;
        private EditText mApproachesQuantity;
        //for keeping data about button state and exercises for training adds.
        private boolean isReadyToAdd;

        public Holder(View itemView) {
            super(itemView);
            mExerciseTitle = (TextView) itemView.findViewById(R.id.fragment_new_training_pattern_exercise_name_tv);
            mExerciseCategory = (TextView) itemView.findViewById(R.id.fragment_new_training_pattern_exercise_category_tv);
            mChoiseButton = (Button) itemView.findViewById(R.id.fragment_new_training_pattern_button);
            mApproachesQuantity = (EditText) itemView.findViewById(R.id.fragment_new_training_pattern_approaches_et);
            isReadyToAdd = true;

        }

        public void setReadyToAdd(boolean flag) {
            isReadyToAdd = flag;
        }

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_new_training_pattern, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        final Exercise exercise = mExerciseBase.get(position);
        holder.mExerciseTitle.setText(exercise.getTitle());
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
        final int positionNow = holder.getAdapterPosition();


        holder.mChoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logf.i("RNTAdapter choiseB listener", "flag in start listener "
                        + holder.isReadyToAdd + " position " + position);
                int temp;
                String approachQuantity = holder.mApproachesQuantity.getText().toString();
                if (!approachQuantity.equals("") && !approachQuantity.matches("\\D+")
                                                 && (temp = Integer.valueOf(approachQuantity)) > 0) {
                    if (temp > 12) {
                        Toast.makeText(mContext, mContext.getString(R.string.quantity_approaches_over_limit),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (holder.isReadyToAdd) {
                            holder.setReadyToAdd(false);
                            holder.mChoiseButton.setBackground(mContext.getResources().getDrawable(R.drawable.remove_red_button));
                            //creating Approaches list for exercise
                            List<Approach> approaches = new ArrayList<Approach>() ;
                            //adding approaches to exercise with auto implementing number of approach
                            for (int i = 0; i <= temp ; i++) {
                                approaches.add(new Approach(i));
                            }
                            exercise.setApproaches(approaches);
                            BusProvider.getInstance().post(new ExerciseForTrainingSelected(exercise, true));
                            Logf.i("RNTAdapter choiseB listener", "flag in choise positive "
                                    + holder.isReadyToAdd + " position " + positionNow);
                        } else {
                            holder.setReadyToAdd(true);
                            holder.mChoiseButton.setBackground(mContext.getResources().getDrawable(R.drawable.add_button));
                            BusProvider.getInstance().post(new ExerciseForTrainingSelected(exercise, false));
                            holder.mApproachesQuantity.setText("");
                            Logf.i("RNTAdapter choiseB listener", "flag in choise negative "
                                    + holder.isReadyToAdd + " position " + positionNow);
                        }

                    }


                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.make_choise_of_approaches_quantity),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mExerciseBase != null ? mExerciseBase.size() : 0;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        BusProvider.getInstance().unregister(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        BusProvider.getInstance().register(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    private void setExerciseBase(List<Exercise> exerciseBase) {
        mExerciseBase = exerciseBase;
    }

}
