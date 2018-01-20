package wang.relish.calendar.pager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;
import java.util.Map;

import wang.relish.calendar.MonthStyle;
import wang.relish.calendar.OnSelectListener;

/**
 * @author Relish Wang
 * @since 2018/01/21
 */
public class MPagerAdapter extends FragmentPagerAdapter {

    private int mSelectedYear, mSelectedMonth, mSelectedDay;

    private Map<String,Integer> mData;
    private OnSelectListener mListener;
    private MPagerAdapter.CurrentPositionGetter mPositionGetter;

    public MPagerAdapter(FragmentManager manager, Map<String, Integer> data, @NonNull OnSelectListener listener, @NonNull MPagerAdapter.CurrentPositionGetter positionGetter) {
        super(manager);
        mData = data;
        mListener = listener;
        mPositionGetter = positionGetter;

        Calendar calendar = Calendar.getInstance();
        mSelectedYear = calendar.get(Calendar.YEAR);
        mSelectedMonth = calendar.get(Calendar.MONTH);
        mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Fragment getItem(int position) {
        int currentPosition = mPositionGetter.getCurrentPosition();
        @Stage int currentStage = mPositionGetter.getCurrentStage();
        int weekFirstDay = mPositionGetter.getWeekFirstDay();
        // 默认的显示样式(仅显示日历，待办事项数为0)
        final MonthStyle monthStyle;
        if (currentPosition == position || currentStage == Stage.FOLD) {
            monthStyle = Utils.createMonthStyle(
                    mSelectedYear,
                    mSelectedMonth,
                    mSelectedDay,
                    weekFirstDay,
                    mData);//来自使用者传入的数据(某天有xx条待办事项)
        } else {
            Calendar date = Utils.getNextFocusDate(
                    position < currentPosition ? Dir.LEFT : Dir.RIGHT,
                    currentStage,
                    mSelectedYear,
                    mSelectedMonth,
                    mSelectedDay,
                    weekFirstDay);
            int year = date.get(Calendar.YEAR);
            int month = date.get(Calendar.MONTH);
            int day = date.get(Calendar.DAY_OF_MONTH);

            monthStyle = Utils.createMonthStyle(
                    year,
                    month,
                    day,
                    weekFirstDay,
                    mData);//来自使用者传入的数据(某天有xx条待办事项)
        }
        MonthFragment fragment = MonthFragment.newInstance(monthStyle);
        if(mListener!=null)fragment.setOnSelectListener(mListener);
        if (mOnTopViewChangedListener != null) {
            fragment.setOnTopViewChangedListener(mOnTopViewChangedListener);
        }
        return fragment;
    }

    public void selectDate(int year, int month, int day) {
        mSelectedYear = year;
        mSelectedMonth = month;
        mSelectedDay = day;
        notifyDataSetChanged();
    }

    public void setNewData(Map<String, Integer> newData) {
        this.mData = newData;
        notifyDataSetChanged();
    }


    public interface CurrentPositionGetter {
        int getCurrentPosition();

        @Stage
        int getCurrentStage();

        int getWeekFirstDay();
    }

    private ITopView.OnTopViewChangedListener mOnTopViewChangedListener;

    public void setOnTopViewChangedListener(ITopView.OnTopViewChangedListener listener) {
        this.mOnTopViewChangedListener = listener;
    }
}
