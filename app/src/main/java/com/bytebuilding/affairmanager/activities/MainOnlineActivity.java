package com.bytebuilding.affairmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.materialdrawer.Drawer;

public class MainOnlineActivity extends AppCompatActivity {

    private Drawer drawer;
    private int selectedDrawerItem = 0;

    private FrameLayout fragmentContainer;

    private Toolbar toolbar;

    private FloatingActionsMenu floatingActionsMenu;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonAddAffair;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonAddGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_online);
        initComponents();
    }

    private void initComponents() {
        fragmentContainer = (FrameLayout) findViewById(R.id.drawer_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    }
}