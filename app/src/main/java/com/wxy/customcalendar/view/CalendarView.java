package com.wxy.customcalendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.wxy.customcalendar.model.DateTagModel;
import com.wxy.customcalendar.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by WangXY on 2015/10/14.10:58.
 */
public class CalendarView extends LinearLayout implements View.OnTouchListener,
        GestureDetector.OnGestureListener, Animation.AnimationListener {

    // 判断手势用
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int caltitleLayoutID = 66; // title布局ID
    // 屏幕宽度和高度
    private int screenWidth;

    // 动画
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    private ViewFlipper viewFlipper;
    private GestureDetector mGesture = null;

    private GridView gView1;//上月
    private GridView gView2;//当月
    private GridView gView3;//下月

    private CalenderViewAdapter gAdapter1;
    private CalenderViewAdapter gAdapter2;
    private CalenderViewAdapter gAdapter3;
    private Context mContext;
    private LinearLayout mMainLayout;
    private int jumpMonth = 0; // 每次点击按钮，增加或减去一个月,默认为0（即显示当前月）
    private int jumpYear = 0; // 点击超过一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private OnCalendarViewListener mListener;
    private String titleDay;
    private List<DateTagModel> list = new ArrayList<>();
    private int hide;
    private int select;
    private String currentYear, currentMonth;
    private int nowYear, nowMonth;
    private int click;


    public CalendarView(Context context) {
        super(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    protected void init() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

        nowYear = Integer.parseInt(currentDate.split("-")[0]);
        nowMonth = Integer.parseInt(currentDate.split("-")[1]);

        currentYear = currentDate.split("-")[0];
        currentMonth = currentDate.split("-")[1];

        // 得到屏幕的宽度
        screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        // 滑动的动画
        slideLeftIn = new TranslateAnimation(screenWidth, 0, 0, 0);
        slideLeftIn.setDuration(300);
        slideLeftIn.setAnimationListener(this);
        slideLeftOut = new TranslateAnimation(0, -screenWidth, 0, 0);
        slideLeftOut.setDuration(300);
        slideLeftOut.setAnimationListener(this);
        slideRightIn = new TranslateAnimation(-screenWidth, 0, 0, 0);
        slideRightIn.setDuration(300);
        slideRightIn.setAnimationListener(this);
        slideRightOut = new TranslateAnimation(0, screenWidth, 0, 0);
        slideRightOut.setDuration(300);
        slideRightOut.setAnimationListener(this);

        // 手势操作
        mGesture = new GestureDetector(mContext, this);

        // 绘制界面
        setOrientation(LinearLayout.HORIZONTAL);
        mMainLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams main_params = new LinearLayout.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        mMainLayout.setLayoutParams(main_params);
        mMainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mMainLayout.setOrientation(LinearLayout.VERTICAL);
        addView(mMainLayout);


        // 底部显示日历
        viewFlipper = new ViewFlipper(mContext);
        RelativeLayout.LayoutParams fliper_params = new RelativeLayout.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        fliper_params.addRule(RelativeLayout.BELOW, caltitleLayoutID);
        mMainLayout.addView(viewFlipper, fliper_params);
        generateClaendarGirdView();
    }

    /**
     * 设置点击日历监听
     *
     * @param listener
     */
    public void setOnCalendarViewListener(OnCalendarViewListener listener) {
        this.mListener = listener;
    }

    public void setValue(int year, int month) {
        year_c = year;
        month_c = month;

        currentYear = year + "";
        currentMonth = month + "";

        day_c = 1;
        jumpMonth = 0;
        jumpYear = 0;
        generateClaendarGirdView();
    }

    public void setTags(List<DateTagModel> list) {
        this.list = list;
        generateClaendarGirdView();
    }

    public void setHide(int hide) {
        this.hide = hide;
        gAdapter2.setHide(hide);
        gAdapter1.setHide(hide);
        gAdapter3.setHide(hide);
        if (viewFlipper.getChildCount() != 0) {
            viewFlipper.removeAllViews();
        }
        viewFlipper.addView(gView2);
        viewFlipper.addView(gView3);
        viewFlipper.addView(gView1);
    }


    /**
     * 生成底部日历
     */
    private void generateClaendarGirdView() {

        gView1 = new CalendarGridView(mContext);
        gAdapter1 = new CalenderViewAdapter(mContext, getResources(), jumpMonth,
                jumpYear, year_c, month_c, day_c, list);
        gAdapter1.setHide(hide);
        if (StringUtil.parseInt(currentYear) == nowYear &&
                StringUtil.parseInt(currentMonth) == nowMonth) {
            gAdapter1.setSeclection(gAdapter1.getCurrentFlag());
        } else {
            gAdapter1.setSeclection(gAdapter1.getStartPositon());
        }
        gView1.setAdapter(gAdapter1);// 设置菜单Adapter

        gView2 = new CalendarGridView(mContext);
        gAdapter2 = new CalenderViewAdapter(mContext, getResources(), jumpMonth,
                jumpYear, year_c, month_c, day_c, list);
        gAdapter2.setHide(hide);
        if (StringUtil.parseInt(currentYear) == nowYear &&
                StringUtil.parseInt(currentMonth) == nowMonth) {
            gAdapter2.setSeclection(gAdapter2.getCurrentFlag());
            click = gAdapter2.getCurrentFlag();
        } else {
            gAdapter2.setSeclection(gAdapter2.getStartPositon());
            click = gAdapter2.getStartPositon();
        }
        gView2.setAdapter(gAdapter2);// 设置菜单Adapter

        gView3 = new CalendarGridView(mContext);
        gAdapter3 = new CalenderViewAdapter(mContext, getResources(), jumpMonth,
                jumpYear, year_c, month_c, day_c, list);
        gAdapter3.setHide(hide);
        if (StringUtil.parseInt(currentYear) == nowYear &&
                StringUtil.parseInt(currentMonth) == nowMonth) {
            gAdapter3.setSeclection(gAdapter2.getCurrentFlag());
        } else {
            gAdapter3.setSeclection(gAdapter3.getStartPositon());
        }
        gView3.setAdapter(gAdapter3);// 设置菜单Adapter

        gView2.setOnTouchListener(this);
        gView1.setOnTouchListener(this);
        gView3.setOnTouchListener(this);

        if (viewFlipper.getChildCount() != 0) {
            viewFlipper.removeAllViews();
        }

        viewFlipper.addView(gView2);
        viewFlipper.addView(gView3);
        viewFlipper.addView(gView1);
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        generateClaendarGirdView();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        int pos = gView2.pointToPosition((int) e.getX(), (int) e.getY());
        if (pos < 0) {
            return false;
        }
        SetClick(gAdapter2, pos);
        return false;
    }

    public void SetClick(CalenderViewAdapter calV, int position) {
        calV.setTag(1);
        int startPosition = calV.getStartPositon();
        int endPosition = calV.getEndPosition();
        calV.notifyDataSetChanged();
        if (hide == 1) {
            position = position % 7 + 7 * (click / 7);
        }
        if (position >= startPosition && position <= endPosition) {
            calV.setSeclection(position);
            click = position;
            titleDay = calV.getDateByClickItem(position).split(
                    "\\.")[0]; // 这一天的阳历
            if (mListener != null) {
                mListener.onCalendarItemClick(this, gAdapter2.getShowYear() + "-" + gAdapter2.getShowMonth() + "-" + titleDay);
            }
        }
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (hide == 1) {
            return false;
        }
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                viewFlipper.setInAnimation(slideLeftIn);
                viewFlipper.setOutAnimation(slideLeftOut);
                viewFlipper.showNext();
                jumpMonth++;
                a(year_c, month_c, jumpMonth);
                if (mListener != null) {
                    mListener.onCalenderScroll(this, currentYear + "年" + currentMonth + "月");
                }
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                viewFlipper.setInAnimation(slideRightIn);
                viewFlipper.setOutAnimation(slideRightOut);
                viewFlipper.showPrevious();
                jumpMonth--;
                a(year_c, month_c, jumpMonth);
                if (mListener != null) {
                    mListener.onCalenderScroll(this, currentYear + "年" + currentMonth + "月");
                }
                return true;
            }

        } catch (Exception e) {
            // nothing
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGesture.onTouchEvent(event);
    }

    private void a(int year, int month, int jump) {
        int stepYear = year;
        int stepMonth = month + jump;
        if (stepMonth > 0) {
            // 往下一个月跳转
            if (stepMonth % 12 == 0) {
                stepYear = year + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            // 往上一个月跳转
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
            if (stepMonth % 12 == 0) {
            }
        }

        currentYear = String.valueOf(stepYear);
        // 得到当前的年份
        currentMonth = String.valueOf(stepMonth); // 得到本月

    }

    public interface OnCalendarViewListener {
        void onCalendarItemClick(CalendarView view, String date);

        void onCalenderScroll(CalendarView view, String date);
    }


}
