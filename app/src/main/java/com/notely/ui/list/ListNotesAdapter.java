package com.notely.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.notely.R;
import com.notely.model.Note;
import com.notely.utility.Helper;
import com.notely.utility.ItemTouchHelperAdapter;

import java.util.List;

/**
 * Created by yashwant on 22/01/18.
 */

public class ListNotesAdapter extends RecyclerView.Adapter<ListNotesAdapter.NoteViewHolder> implements ItemTouchHelperAdapter {

    private List<Note> noteArrayList;
    private ListNotesAdapterActionListener adapterActionListener;

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
        holder.tvDate.setText("" + Helper.INSTANCE.getDate(holder.tvDate.getContext(), note.getTime_created()));
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
        noteArrayList.clear();
        this.noteArrayList.addAll(notes);
        notifyDataSetChanged();
    }

    public void filterList(List<Note> notes) {
        noteArrayList.clear();
        this.noteArrayList.addAll(notes);
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {
        if (position != RecyclerView.NO_POSITION) {
            adapterActionListener.onItemSwipe(noteArrayList.get(position), position);
        }
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvGist;
        TextView tvDate;
        ImageView ivStar;
        ImageView ivFavourite;
        RelativeLayout backGround;
        public RelativeLayout foreGround;


        public NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvGist = itemView.findViewById(R.id.tvGist);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivStar = itemView.findViewById(R.id.ivStar);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            backGround = itemView.findViewById(R.id.background);
            foreGround = itemView.findViewById(R.id.foreGround);


            foreGround.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    adapterActionListener.onListItemClicked(noteArrayList.get(getAdapterPosition()));
                }
            });

            ivStar.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    if (noteArrayList.get(getAdapterPosition()).isStar()) {
                        noteArrayList.get(getAdapterPosition()).setStar(false);
                    } else {
                        noteArrayList.get(getAdapterPosition()).setStar(true);
                    }
                    notifyItemChanged(getAdapterPosition());
                    adapterActionListener.onStarOrFavClicked(noteArrayList.get(getAdapterPosition()), getAdapterPosition());

                }
            });

            ivFavourite.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    if (noteArrayList.get(getAdapterPosition()).isFavourite()) {
                        noteArrayList.get(getAdapterPosition()).setFavourite(false);
                    } else {
                        noteArrayList.get(getAdapterPosition()).setFavourite(true);
                    }
                    notifyItemChanged(getAdapterPosition());
                    adapterActionListener.onStarOrFavClicked(noteArrayList.get(getAdapterPosition()), getAdapterPosition());

                }
            });
        }
    }

    public interface ListNotesAdapterActionListener {
        void onListItemClicked(Note note);

        void onItemSwipe(Note note, int position);

        void onStarOrFavClicked(Note note, int position);

    }

    public void setAdapterActionListener(ListNotesAdapterActionListener adapterActionListener) {
        this.adapterActionListener = adapterActionListener;
    }

    public void removeItem(int position) {
        noteArrayList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Note item, int position) {
        noteArrayList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
