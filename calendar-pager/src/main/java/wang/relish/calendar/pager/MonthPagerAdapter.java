package wang.relish.calendar.pager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import wang.relish.calendar.MonthStyle;
import wang.relish.calendar.OnSelectListener;
import wang.relish.calendar.pager.viewpager.RecyclerViewPager;

/**
 * 日历的ViewPager的Adapter
 *
 * @author Relish Wang
 * @since 2017/11/20
 */
public class MonthPagerAdapter extends RecyclerViewPager.Adapter<MonthPagerAdapter.MonthHolder> {

    /**
     * 记录当前显示模式的第一天
     * <p>
     * 周模式记录第一个格子的日期, 例:
     * 2017年4月<p>
     * 27 28 29 30  1  2  3 <p>
     * (记录: 2017年4月27日)<p>
     * <p>
     * 月模式记录第一个格子的日期, 例:
     * 2017年5月<p>
     * 27 28 29 30  1  2  3<p>
     * 4  5  6  7  8  9 10<p>
     * 11 12 13 14 15 16 17<p>
     * 18 19 20 21 22 23 24<p>
     * 25 26 27 28 29 30 31<p>
     * 1  2  3  4  5  6  7<p>
     * (记录: 2017年4月27日)
     */
    private int mSelectedYear, mSelectedMonth, mSelectedDay;

    /**
     * {
     * "20171222":123,
     * "20180001":34,
     * "20180002":1,
     * "20180003":3
     * }
     */
    private Map<String, Integer> mData = new HashMap<>();

    private OnSelectListener mOnSelectListener;
    private ITopView.OnTopViewChangedListener mOnTopViewChangedListener;
    private CurrentPositionGetter mPositionGetter;

    public MonthPagerAdapter(Map<String, Integer> data, OnSelectListener listener, CurrentPositionGetter positionGetter) {
        mData = data;
        if (listener == null)
            throw new NullPointerException("OnSelectListener should not be null!");
        mOnSelectListener = listener;
        if (positionGetter == null)
            throw new NullPointerException("CurrentPositionGetter should not be null!");
        mPositionGetter = positionGetter;

        Calendar calendar = Calendar.getInstance();
        mSelectedYear = calendar.get(Calendar.YEAR);
        mSelectedMonth = calendar.get(Calendar.MONTH);
        mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagerlib_item_month, parent, false);
        return new MonthHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MonthHolder holder, int position) {
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
        PMonthAdapter adapter = new PMonthAdapter(monthStyle);
        holder.monthView.setAdapter(adapter);
        if (mOnSelectListener != null) {
            holder.monthView.setOnSelectListener(mOnSelectListener);
        }
        if (mOnTopViewChangedListener != null) {
            holder.monthView.setOnTopViewChangedListener(mOnTopViewChangedListener);
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public void setOnTopViewChangedListener(ITopView.OnTopViewChangedListener listener) {
        this.mOnTopViewChangedListener = listener;
    }

    class MonthHolder extends RecyclerViewPager.ViewHolder {

        PMonthView monthView;

        private MonthHolder(View itemView) {
            super(itemView);
            monthView = itemView.findViewById(R.id.mv);
        }
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
}
