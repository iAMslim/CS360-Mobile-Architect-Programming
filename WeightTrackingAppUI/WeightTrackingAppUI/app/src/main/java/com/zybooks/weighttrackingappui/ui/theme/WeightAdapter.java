package com.zybooks.weighttrackingappui.ui.theme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.weighttrackingappui.R;

import java.util.List;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder> {

    private final List<WeightEntry> weightList;
    private final OnWeightClickListener clickListener;
    private final OnWeightLongClickListener longClickListener;

    public interface OnWeightClickListener {
        void onWeightClick(WeightEntry entry);
    }

    public interface OnWeightLongClickListener {
        void onWeightLongClick(WeightEntry entry);
    }

    public WeightAdapter(List<WeightEntry> weightList, OnWeightClickListener clickListener,
                         OnWeightLongClickListener longClickListener) {
        this.weightList = weightList;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateText, weightText;

        public ViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.textDate);
            weightText = itemView.findViewById(R.id.textWeight);
        }

        public void bind(WeightEntry entry, OnWeightClickListener clickListener,
                         OnWeightLongClickListener longClickListener) {
            itemView.setOnClickListener(v -> clickListener.onWeightClick(entry));
            itemView.setOnLongClickListener(v -> {
                longClickListener.onWeightLongClick(entry);
                return true;
            });
        }
    }

    @NonNull
    @Override
    public WeightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weight, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightAdapter.ViewHolder holder, int position) {
        WeightEntry entry = weightList.get(position);
        holder.dateText.setText("Date: " + entry.getDate());
        holder.weightText.setText("Weight: " + entry.getWeight() + " lbs");
        holder.bind(entry, clickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return weightList.size();
    }
}
