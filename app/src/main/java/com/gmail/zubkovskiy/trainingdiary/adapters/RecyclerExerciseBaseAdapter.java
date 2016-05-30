package com.gmail.zubkovskiy.trainingdiary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.events.ExerciseInBaseSelected;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;

import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 08.05.2016.
 */
public class RecyclerExerciseBaseAdapter extends RecyclerView.Adapter<RecyclerExerciseBaseAdapter.Holder> {

    private List<Exercise> mExerciseBase;
   // private Context mContext;

   public RecyclerExerciseBaseAdapter(List<Exercise> mExerciseBase, Context context) {

        this.mExerciseBase = mExerciseBase;
        //this.mContext = context;

    }

    protected static class Holder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mCategory;
        private LinearLayout mClickableLayout;

        public Holder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.exercise_base_list_pattern_name_tv);
            mCategory = (TextView) itemView.findViewById(R.id.exercise_base_list_pattern_category_tv);
            mClickableLayout = (LinearLayout) itemView.findViewById(R.id.exercise_base_list_pattern_item_layout);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
     //   BusProvider.getInstance().unregister(this);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_base_list_pattern,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {

        Exercise exercise = mExerciseBase.get(position);
        String category = exercise.getCategory();
        switch (category){
            case Exercise.CATEGORY_ARMS:
                holder.mCategory.setText(R.string.ARMS);
                break;
            case Exercise.CATEGORY_BACK:
                holder.mCategory.setText(R.string.BACK);
                break;
            case Exercise.CATEGORY_LEGS:
                holder.mCategory.setText(R.string.LEGS);
                break;
            case Exercise.CATEGORY_SHOULDERS:
                holder.mCategory.setText(R.string.SHOULDERS);
                break;
            case Exercise.CATEGORY_CHEST:
                holder.mCategory.setText(R.string.CHEST);
                break;
            default:
                holder.mCategory.setText(category);
        }
        holder.mTitle.setText(exercise.getTitle());
        holder.mClickableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This busProvider use to show exercise description by FragmentExerciseDescription in ExerciseBaseActivity
                BusProvider.getInstance().post(new ExerciseInBaseSelected(mExerciseBase.get(position)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExerciseBase != null ? mExerciseBase.size() : 0;
    }

    @Override
    public void onViewDetachedFromWindow(Holder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(Holder holder) {
        BusProvider.getInstance().register(this);
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        BusProvider.getInstance().unregister(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
