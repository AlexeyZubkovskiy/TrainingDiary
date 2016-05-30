package com.gmail.zubkovskiy.trainingdiary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.model.Approach;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 22.05.2016.
 */
public class RecyclerTrainingAdapter extends RecyclerView.Adapter<RecyclerTrainingAdapter.Holder> {

    private static RecyclerTrainingAdapter mAdapterInstance;
    private List<Approach> mExerciseApproaches;
    private Context mContext;
    private HashMap<Integer, EditText[]> mApproachesData;

    private RecyclerTrainingAdapter(List<Approach> approaches, Context context) {
        mExerciseApproaches = approaches;
        mContext = context;
    }

    public static RecyclerTrainingAdapter getInstance(List<Approach> approaches, Context context) {

        if (mAdapterInstance == null) {
            mAdapterInstance = new RecyclerTrainingAdapter(approaches, context);
        } else {
            mAdapterInstance.mExerciseApproaches = approaches;
            mAdapterInstance.mContext = context;
        }
        //to garanted have sorted approaches in Exercise
        Collections.sort(approaches, Approach.sortApproaches);
        mAdapterInstance.mApproachesData = new HashMap<>();

        return mAdapterInstance;
    }

    protected static class Holder extends RecyclerView.ViewHolder {

        private EditText mApproachWeight;
        private EditText mApproachQuantity;
        private TextView mApproachNumber;

        public Holder(View itemView) {
            super(itemView);
            mApproachNumber = (TextView) itemView.findViewById(R.id.fragment_training_pattern_tv);
            mApproachWeight = (EditText) itemView.findViewById(R.id.fragment_training_pattern_et);
            mApproachQuantity = (EditText) itemView.findViewById(R.id.fragment_training_pattern_quantity_et);
        }
    }

    @Override
    public RecyclerTrainingAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_training_pattern, parent, false);

        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerTrainingAdapter.Holder holder, int position) {

        Approach approach = mExerciseApproaches.get(position);
        EditText[] data = {holder.mApproachWeight, holder.mApproachQuantity};
        mApproachesData.put(position, data);
        //to set weight and repeats if its implemented
        if (approach.getWeight() > 0)
            holder.mApproachWeight.setText(String.valueOf(approach.getWeight()));
        if (approach.getRepeats() > 0)
            holder.mApproachQuantity.setText(String.valueOf(approach.getRepeats()));

        String approachNumber = mContext.getString(R.string.approach) + " " + (position + 1) + " :";
        holder.mApproachNumber.setText(approachNumber);

        {//set listeners to auto implement values to approach
            holder.mApproachWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String data = s.toString();
                    if (data.matches("\\d+.?\\d*")) {
                        mExerciseApproaches.get(holder.getAdapterPosition()).setWeight(Float.valueOf(data));
                        Logf.e("RTA onBindViewHolder:", "TEXT CHANGED LISTENER approachWeight");
                    }
                }
            });

            holder.mApproachQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String data = s.toString();
                    if (data.matches("\\d+")) {
                        mExerciseApproaches.get(holder.getAdapterPosition()).setRepeats(Integer.valueOf(data));
                        Logf.e("RTA onBindViewHolder:", "TEXT CHANGED LISTENER approachQuantity");
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mExerciseApproaches != null ? mExerciseApproaches.size() : 0;
    }
}
