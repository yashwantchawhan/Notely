package com.notely.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.notely.R;

public class DetailsNoteActivity extends AppCompatActivity {

    private Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu=menu;
        getMenuInflater().inflate(R.menu.details_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_undo:
                break;
            case R.id.action_save:
                mMenu.findItem(R.id.action_undo).setVisible(false);
                mMenu.findItem(R.id.action_save).setVisible(false);
                mMenu.findItem(R.id.action_edit).setVisible(true);
                break;
            case R.id.action_edit:
                mMenu.findItem(R.id.action_undo).setVisible(true);
                mMenu.findItem(R.id.action_save).setVisible(true);
                mMenu.findItem(R.id.action_edit).setVisible(false);

                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
