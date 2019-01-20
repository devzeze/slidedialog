package com.dawnlight.slidedialogdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dawnlight.slidedialog.SlideDialog;

public class SlideDialogDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_dialog_demo);

        View view = findViewById(R.id.root_demo_view);
        view.setOnClickListener((View v) -> {
            SlideDialog.newInstance(this)
                    .message("Alert Message")
                    .show();
        });
    }
}
