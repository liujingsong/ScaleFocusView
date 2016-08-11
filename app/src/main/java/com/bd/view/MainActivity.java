package com.bd.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bd.ui.R;

public class MainActivity extends Activity {

    private FocusView mFocusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFocusView();
            }
        });
        showFocusView();

    }

    private void showFocusView() {
        FocusView focusView = new FocusView(this);
        int[] metrics = getDeviceMetrics(this);
        /* 初始化为屏幕中心位置，根据自己业务需求更改 */
        focusView.setCenter(metrics[0] / 2, metrics[1] / 2);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addContentView(focusView, layoutParams);
    }

    public int[] getDeviceMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return new int[] { screenWidth, screenHeight };
    }


}
