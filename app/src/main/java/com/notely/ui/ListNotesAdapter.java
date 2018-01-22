package com.notely.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.notely.R;
import com.notely.model.Note;

import java.util.List;

/**
 * Created by yashwant on 22/01/18.
 */

public class ListNotesAdapter extends RecyclerView.Adapter<ListNotesAdapter.NoteViewHolder> {

    private List<Note> noteArrayList;

    public ListNotesAdapter(List<Note> noteArrayList) {
        this.noteArrayList = noteArrayList;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        final Note note = noteArrayList.get(position);
        holder.tvTitle.setText("" + note.getTitle());
        holder.tvGist.setText("" + note.getGist());
        holder.tvDate.setText("" + note.getTime_created());

        if (note.isStar()) {
            holder.ivStar.setImageDrawable(holder.ivStar.getContext().getDrawable(R.drawable.ic_star));
        } else {
            holder.ivStar.setImageDrawable(holder.ivStar.getContext().getDrawable(R.drawable.ic_not_star));
        }

        if (note.isFavourite()) {
            holder.ivFavourite.setImageDrawable(holder.ivStar.getContext().getDrawable(R.drawable.ic_favorite));
        } else {
            holder.ivFavourite.setImageDrawable(holder.ivStar.getContext().getDrawable(R.drawable.ic_not_favorite));
        }
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public void addItems(List<Note> notes) {
        this.noteArrayList = notes;
        notifyDataSetChanged();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvGist;
        TextView tvDate;
        ImageView ivStar;
        ImageView ivFavourite;
        ViewGroup viewGroupParent;

        public NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvGist = itemView.findViewById(R.id.tvGist);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivStar = itemView.findViewById(R.id.ivStar);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            viewGroupParent=itemView.findViewById(R.id.rlParent);


            viewGroupParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                    }
                }
            });

            ivStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                    }
                }
            });

            ivFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                    }
                }
            });
        }
    }
}
