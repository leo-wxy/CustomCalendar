package com.wxy.customcalendar.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxy.customcalendar.R;
import com.wxy.customcalendar.model.DateTagModel;
import com.wxy.customcalendar.utils.CalendarUtil;
import com.wxy.customcalendar.utils.DateUtil;
import com.wxy.customcalendar.utils.SpecialCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



/**
 * ClassName:CalendarView Function:TODO ADD FUNCTION.〈一句话功能简述〉. <br/>
 * Description: TODO 〈功能详细描述，包括界面的上层以及下层的逻辑关系〉. <br/>
 * @author WangXY
 * @since [产品/模块版本] （可选）
 */
public class CalenderViewAdapter extends BaseAdapter {

    SpecialCalendar spe = new SpecialCalendar();
    private boolean isLeapyear = false; // 是否为闰年
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几
    private int lastDaysOfMonth = 0; // 上一个月的总天数
    private Context context;
    private String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中
    private String[] dateNumber = new String[42];
    private String[] weekNumber = new String[7];
    private List<DateTagModel> list = new ArrayList<>();
    private SpecialCalendar sc = null;
    private Resources res = null;
    private Drawable drawable = null;
    private String currentYear = "";
    private String currentMonth = "";
    private String currentDay = "";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    // private int[] schDateTagFlag = null; // 存储当月所有的日程日期
    private int currentFlag = -1; // 用于标记当天
    private String showYear = ""; // 用于在头部显示的年份
    private String showMonth = ""; // 用于在头部显示的月份
    private String animalsYear = "";
    private String leapMonth = ""; // 闰哪一个月
    private String cyclical = ""; // 天干地支
    // 系统当前时间
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";
    private int totalDay = 42;
    private int tag = 0;
    // 日程时间(需要标记的日程日期)
    private int clickTemp = -1;
    private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
    private int hide = 0;
    private int today = 0;


    public CalenderViewAdapter() {
        Date date = new Date();
        sysDate = sdf.format(date); // 当期日期
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
    }

    public CalenderViewAdapter(Context context, Resources rs, int jumpMonth,
                               int jumpYear, int year_c, int month_c, int day_c, List<DateTagModel> list) {
        this();
        this.context = context;
        sc = new SpecialCalendar();
        this.res = rs;

        int stepYear = year_c + jumpYear;
        int stepMonth = month_c + jumpMonth;
        if (stepMonth > 0) {
            // 往下一个月跳转
            if (stepMonth % 12 == 0) {
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year_c + stepMonth / 12;
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
        // （jumpMonth为跳动的次数，每滑动一次就增加一月或减一月）
        currentDay = String.valueOf(day_c); // 得到当前日期是哪天

        this.list = list;

        getCalendar(Integer.parseInt(currentYear),
                Integer.parseInt(currentMonth));

    }

    public CalenderViewAdapter(Context context, Resources rs, int year, int month,
                               int day) {
        this();
        this.context = context;
        sc = new SpecialCalendar();
        this.res = rs;
        currentYear = String.valueOf(year);
        // 得到跳转到的年份
        currentMonth = String.valueOf(month); // 得到跳转到的月份
        currentDay = String.valueOf(day); // 得到跳转到的天
        getCalendar(Integer.parseInt(currentYear),
                Integer.parseInt(currentMonth));
    }

    public int getHide() {
        return hide;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    public int getToday() {
        return today;
    }

    public void setToday(int today) {
        this.today = today;
    }

    public void setSeclection(int position) {
        clickTemp = position;
    }

    public int getSelection() {
        return clickTemp;
    }

    @Override
    public int getCount() {
        return getHide() == 0 ? totalDay : 7;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View rowView = view;
        ViewHolder holder = null;
        if (rowView == null) {
            holder = new ViewHolder();
            rowView = LayoutInflater.from(context).inflate(
                    R.layout.item_cal, null);
            holder.rl_cal = (RelativeLayout) rowView.findViewById(R.id.rl_cal);
            holder.ll_info = (LinearLayout) rowView.findViewById(R.id.ll_info);
            holder.tv_date = (TextView) rowView.findViewById(R.id.tv_date);
            holder.tv_name = (TextView) rowView.findViewById(R.id.tv_name);
            holder.tv_tip = (TextView) rowView.findViewById(R.id.tv_tip);
            holder.tv_status = (TextView) rowView.findViewById(R.id.tv_status);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        String d;
        Date myDate;
        if (hide == 1) {
            d = dayNumber[position + (clickTemp / 7) * 7].split("\\.")[0];
            myDate = DateUtil.strToDate(dateNumber[position + (clickTemp / 7) * 7]);
        } else {
            d = dayNumber[position].split("\\.")[0];
            myDate = DateUtil.strToDate(dateNumber[position]);
        }

        SpannableString sp = new SpannableString(d);
        holder.tv_date.setText(sp);

        Calendar calCalendar = Calendar.getInstance();
        calCalendar.setTime(myDate);
        CalendarUtil calendarUtil = new CalendarUtil(calCalendar);
        holder.tv_name.setText(calendarUtil.toString());
        holder.tv_status.setVisibility(View.GONE);
        holder.tv_tip.setVisibility(View.INVISIBLE);


        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getDay().equals(d)) {
                    holder.tv_tip.setVisibility(View.VISIBLE);
                }
            }
        } else {
            holder.tv_tip.setVisibility(View.INVISIBLE);
        }

        if (hide == 1) {
            position = position + (clickTemp / 7) * 7;
        }
        if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
            // 当前月信息显示
            holder.rl_cal.setVisibility(View.VISIBLE);
            holder.tv_date.setTextColor(Color.BLACK);// 当月字体设黑
            if (clickTemp == position) {
                holder.ll_info.setBackgroundResource(R.drawable.circle_cf39800);
                holder.tv_date.setTextColor(Color.WHITE);
                holder.tv_name.setTextColor(Color.WHITE);
            } else {
                holder.ll_info.setBackgroundColor(Color.WHITE);
                holder.tv_date.setTextColor(context.getResources().getColor(R.color.c414141));
                holder.tv_name.setTextColor(context.getResources().getColor(R.color.c414141));
            }
        } else {
            holder.rl_cal.setVisibility(View.INVISIBLE);
        }

        if (getTag() == 0) {
            if (currentFlag == position) {
                // 设置当天的背景
                holder.tv_date.setTextColor(Color.WHITE);
                holder.tv_name.setTextColor(Color.WHITE);
            }
        }
        if (position < dayOfWeek || position >= daysOfMonth + dayOfWeek) {
            holder.tv_date.setTextColor(context.getResources().getColor(R.color.cd0d0d0));
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.cd0d0d0));
        }
        return rowView;
    }

    // 得到某年的某月的天数且这月的第一天是星期几
    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year); // 是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
        lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
        Log.d("DAY", isLeapyear + " ======  " + daysOfMonth
                + "  ============  " + dayOfWeek + "  =========   "
                + lastDaysOfMonth);
        getweek(year, month);
    }

    // 将一个月中的每一天的值添加入数组dayNuber中
    private void getweek(int year, int month) {
        int j = 1;
        int flag = 0;
        int CMonth = month;
        int CYear = year;
        int a = 0;
        int b = 0;

        // 得到当前月的所有日程日期(这些日期需要标记)
        // dao = new ScheduleDAO(context);

        for (int i = 0; i < dayNumber.length; i++) {
            // 周一
            if (i < dayOfWeek) { // 前一个月
                int temp = lastDaysOfMonth - dayOfWeek + 1;
                dayNumber[i] = (temp + i) + ".";
                if (a == 0) {
                    month = CMonth;
                    year = CYear;
                    month = month - 1;
                    if (month == 1) {
                        month = 12;
                        year = year - 1;
                    }
                    a = 1;
                }
                dateNumber[i] = year + "-" + month + "-" + dayNumber[i].split("\\.")[0];
            } else if (i < daysOfMonth + dayOfWeek) { // 本月
                String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期
                month = CMonth;
                year = CYear;
                dayNumber[i] = i - dayOfWeek + 1 + ".";
                dateNumber[i] = CYear + "-" + CMonth + "-" + dayNumber[i].split("\\.")[0];
                // 对于当前月才去标记当前日期
                if (sys_year.equals(String.valueOf(year))
                        && sys_month.equals(String.valueOf(month))
                        && sys_day.equals(day)) {
                    // 笔记当前日期
                    currentFlag = i;
                }
                // 标记日程日期

                setShowYear(String.valueOf(year));
                setShowMonth(String.valueOf(month));
                if (dayNumber[i].equals("1.")) {
                    totalDay = i + daysOfMonth;
                }
            } else { // 下一个月
                if (b == 0) {
                    month = CMonth;
                    year = CYear;
                    month = month + 1;
                    if (month == 12) {
                        month = 1;
                        year = year + 1;
                    }
                }
                dayNumber[i] = j + "";
                j++;
                dateNumber[i] = year + "-" + month + "-" + dayNumber[i].split("\\.")[0];
            }

        }

        String abc = "";
        for (int i = 0; i < dayNumber.length; i++) {
            abc = abc + dayNumber[i] + ":";
        }
        Log.d("DAYNUMBER", abc);

    }

    public void matchScheduleDate(int year, int month, int day) {

    }

    /**
     * 点击每一个item时返回item中的日期
     *
     * @param position
     * @return
     */
    public String getDateByClickItem(int position) {
        return dayNumber[position];
    }

    /**
     * 在点击gridView时，得到这个月中第一天的位置
     *
     * @return
     */
    public int getStartPositon() {
        return dayOfWeek;
    }

    /**
     * 在点击gridView时，得到这个月中最后一天的位置
     *
     * @return
     */
    public int getEndPosition() {
        return (dayOfWeek + daysOfMonth) - 1;
    }

    public String getShowYear() {
        return showYear;
    }

    public void setShowYear(String showYear) {
        this.showYear = showYear;
    }

    public String getShowMonth() {
        return showMonth;
    }

    public void setShowMonth(String showMonth) {
        this.showMonth = showMonth;
    }

    public String getAnimalsYear() {
        return animalsYear;
    }

    public void setAnimalsYear(String animalsYear) {
        this.animalsYear = animalsYear;
    }

    public String getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(String leapMonth) {
        this.leapMonth = leapMonth;
    }

    public String getCyclical() {
        return cyclical;
    }

    public void setCyclical(String cyclical) {
        this.cyclical = cyclical;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getCurrentFlag() {
        return currentFlag;
    }

    private class ViewHolder {
        LinearLayout ll_info;
        TextView tv_date, tv_name, tv_tip, tv_status;
        RelativeLayout rl_cal;
    }
}


