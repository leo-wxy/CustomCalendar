package com.wxy.customcalendar;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wxy.customcalendar.databinding.ActivityMainBinding;
import com.wxy.customcalendar.view.CalendarView;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    TextView tv;
    CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        tv = activityMainBinding.tvSelectTime;
        mCalendarView = activityMainBinding.calView;

        mCalendarView.setOnCalendarViewListener(new CalendarView.OnCalendarViewListener() {
            @Override
            public void onCalendarItemClick(CalendarView view, String date) {
                tv.setText(date);
            }

            @Override
            public void onCalenderScroll(CalendarView view, String date) {

            }
        });
    }


}
