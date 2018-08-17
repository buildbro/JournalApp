package com.buildbrothers.android.journalapp;

import android.view.View;

/**
 * Created by EA on 6/29/2018.
 */

public interface RecyclerViewClickListener {
    void onRowClicked(int position);
    void onViewClicked(View v, int position);
}
