package com.bd.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.bd.ui.R;

public class MainActivity extends Activity {

    private FocusView mFocusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFocusView = (FocusView) findViewById(R.id.focus_view);
    }


}
