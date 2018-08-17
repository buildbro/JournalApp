package com.buildbrothers.android.journalapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by EA on 6/28/2018.
 */

public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.JournalViewHolder>{

    private RecyclerViewClickListener listener;

    class JournalViewHolder extends RecyclerView.ViewHolder {
        private final TextView textLabel, dateLabel;
        private final ImageView deleteIco;

        private JournalViewHolder(View itemView, final RecyclerViewClickListener listener) {
            super(itemView);
            textLabel = itemView.findViewById(R.id.text);
            dateLabel = itemView.findViewById(R.id.date);

            deleteIco = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onRowClicked(getAdapterPosition());
                }
            });

            deleteIco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onViewClicked(v, getAdapterPosition());
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<DiaryEntity> mDiaryEntities; // Cached copy of words

    JournalListAdapter(Context context, RecyclerViewClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, parent, false);
        return new JournalViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(JournalViewHolder holder, int position) {
        if (mDiaryEntities != null) {
            DiaryEntity current = mDiaryEntities.get(position);
            holder.textLabel.setText(current.getText());
            holder.dateLabel.setText(current.getDate_added());
        } else {
            // Covers the case of data not being ready yet.
            holder.textLabel.setText("No Content");
        }
    }

    void setWords(List<DiaryEntity> diaryEntityList){
        mDiaryEntities = diaryEntityList;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mDiaryEntities != null)
            return mDiaryEntities.size();
        else return 0;
    }

    //return journal details to help onItemClick get values at current position
    public List<DiaryEntity> getJournals() {
        return mDiaryEntities;
    }
}
