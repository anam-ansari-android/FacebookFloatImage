package com.anam.floatimage.view;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.anam.floatimage.R;
import com.anam.floatimage.model.FloatingCircle;

/**
 * @author Anam Ansari
 * 18 January 2018
 */
public class MainActivity extends AppCompatActivity {


    private static final int OVERLAY_PERMISSION_REQ_CODE = 100;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = MainActivity.this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //add the service here
                //make the button gone

                if ((Build.VERSION.SDK_INT >= 23)) {
                    if (!Settings.canDrawOverlays(context)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                    } else if (Settings.canDrawOverlays(context)) {
                        startService(new Intent(context, FloatingCircle.class));
                    }
                }
                else
                {
                    startService(new Intent(context, FloatingCircle.class));
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            startService(new Intent(context, FloatingCircle.class));

        }
    }


}
