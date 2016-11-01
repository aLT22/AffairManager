package com.bytebuilding.affairmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {

    private FloatingActionsMenu floatingActionsMenu;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonAddAffair;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonAddGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        floatingActionButtonAddAffair = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.fab_add_affair);
        floatingActionButtonAddAffair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Add Affair Clicked", Toast.LENGTH_LONG)
                        .show();
                floatingActionsMenu.collapse();
            }
        });

        floatingActionButtonAddGroup = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.fab_add_group);
        floatingActionButtonAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Add Group Clicked", Toast.LENGTH_LONG)
                        .show();
                floatingActionsMenu.collapse();
            }
        });
        /*floatingActionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
}