package com.buildbrothers.android.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private DiaryViewModel mDiaryViewModel;
    JournalListAdapter adapter;

    FirebaseAuth mAuth;

    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAdd();
            }
        });

        mDiaryViewModel = ViewModelProviders.of(this).get(DiaryViewModel.class);

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new JournalListAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDiaryViewModel.getAllJournals().observe(this, new Observer<List<DiaryEntity>>() {
            @Override
            public void onChanged(@Nullable final List<DiaryEntity> journals) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(journals);

                //display or hide empty journal method
                TextView emptyView = (TextView) findViewById(R.id.empty_view);
                if (adapter.getItemCount()==0) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_layout);

        mAuth = FirebaseAuth.getInstance();
        checkLogin();
    }

    //this methods creates a dialog that the user can use to enter new journal record
    public void goAdd() {
        AlertDialog.Builder mBuilderC = new AlertDialog.Builder(MainActivity.this);
        View mViewC = getLayoutInflater().inflate(R.layout.dialog_add, null);
        final EditText messageEditText = (EditText) mViewC.findViewById(R.id.text);
        Button submit = (Button) mViewC.findViewById(R.id.btnSnd);
        Button dismiss = (Button) mViewC.findViewById(R.id.btnCancel);
        mBuilderC.setView(mViewC);
        final AlertDialog dialogC = mBuilderC.create();
        dialogC.show();


        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialogC.dismiss();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!messageEditText.getText().toString().isEmpty()){
                    String ts = currentDate();
                    DiaryEntity diaryEntity = new DiaryEntity(0, messageEditText.getText().toString(), ts, "0", 0);
                    mDiaryViewModel.insert(diaryEntity);
                    dialogC.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please enter some text",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //helper method that returns string of current date in clean formart
    public String currentDate() {
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String dateString = dtf.format(date);

        return dateString;
    }

    @Override
    public void onViewClicked(View v, int position) {
        List<DiaryEntity> current = adapter.getJournals();
        final DiaryEntity currentEntity = current.get(position);
        int id = currentEntity.getId();
        if(v.getId() == R.id.delete){
            // Show confirm dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Delete Journal");
            builder.setMessage("Do you want to stop ?");
            builder.setIcon(R.drawable.ic_delete_grey600_18dp);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    mDiaryViewModel.delete(currentEntity);

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    //handle list row click
    @Override
    public void onRowClicked(int position) {

        // Clicked entire row
        List<DiaryEntity> current = adapter.getJournals();
        final DiaryEntity currentEntity = current.get(position);
        int id = currentEntity.getId();
        final String mainText = currentEntity.getText();
        String dateCreated = currentEntity.getDate_added();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.details_dialog, null);
        TextView mTextView = (TextView)mView.findViewById(R.id.main_text);
        TextView DetailsTV = (TextView)mView.findViewById(R.id.details);
        ImageView edit = (ImageView) mView.findViewById(R.id.edit);

        DetailsTV.setText("Created "+dateCreated);
        mTextView.setText(mainText);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        //dialog buttons control
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AlertDialog.Builder mBuilderC = new AlertDialog.Builder(MainActivity.this);
                View mViewC = getLayoutInflater().inflate(R.layout.dialog_edit, null);
                final EditText messageEditText = (EditText) mViewC.findViewById(R.id.text);
                Button submit = (Button) mViewC.findViewById(R.id.btnSnd);
                Button dismiss = (Button) mViewC.findViewById(R.id.btnCancel);

                messageEditText.setText(mainText);
                mBuilderC.setView(mViewC);
                final AlertDialog dialogC = mBuilderC.create();
                dialogC.show();
                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogC.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!messageEditText.getText().toString().isEmpty()){
                            DiaryEntity updateEntity = new DiaryEntity(currentEntity.getId(), messageEditText.getText().toString(), currentEntity.getDate_added(), "0", 0);
                            mDiaryViewModel.update(updateEntity);
                            dialogC.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //helper method to check if user is signed in
    public void checkLogin() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null) {
            // Show confirm dialog
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Hello there! Please sign in to get more personalised experience ", Snackbar.LENGTH_LONG)
                    .setAction("Sign-in", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(MainActivity.this, AccountActivity.class);
                            MainActivity.this.startActivity(i);
                        }
                    });

            snackbar.show();
        }else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Welcome back "+currentUser.getDisplayName(), Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, AccountActivity.class);
            MainActivity.this.startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
