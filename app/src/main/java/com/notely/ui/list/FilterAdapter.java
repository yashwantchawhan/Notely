package com.notely.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.notely.R;
import com.notely.model.Filter;

import java.util.ArrayList;

/**
 * Created by yashwant on 25/01/18.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private final ArrayList<Filter> filterArrayList;

    public FilterAdapter(ArrayList<Filter> filterArrayList) {
        this.filterArrayList = filterArrayList;
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {

        holder.tvFilterTitle.setText("" + filterArrayList.get(position).getFilterType().toString());
        if (filterArrayList.get(position).isChecked()) {
            holder.ivFilterCheck.setImageDrawable(holder.ivFilterCheck.getResources().getDrawable(R.drawable.ic_check));
            holder.tvFilterTitle.setTextColor(holder.tvFilterTitle.getContext().getResources().getColor(R.color.filter_check));

        } else {
            holder.tvFilterTitle.setTextColor(holder.tvFilterTitle.getContext().getResources().getColor(R.color.white));
            holder.ivFilterCheck.setImageDrawable(holder.ivFilterCheck.getResources().getDrawable(R.drawable.ic_uncheck));
        }

    }


    @Override
    public int getItemCount() {
        return filterArrayList.size();
    }

    public ArrayList<Filter> getFilterList() {
        return filterArrayList;
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFilterTitle;
        ImageView ivFilterCheck;
        LinearLayout llFilterItemParent;

        public FilterViewHolder(View itemView) {
            super(itemView);
            tvFilterTitle = itemView.findViewById(R.id.tvFilterTitle);
            ivFilterCheck = itemView.findViewById(R.id.ivFilterCheck);
            llFilterItemParent = itemView.findViewById(R.id.llFilterItemParent);
            llFilterItemParent.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    if (filterArrayList.get(getAdapterPosition()).isChecked()) {
                        filterArrayList.get(getAdapterPosition()).setChecked(false);
                    } else {
                        filterArrayList.get(getAdapterPosition()).setChecked(true);
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
