package wang.relish.datepicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.WeakHashMap;

import wang.relish.datepicker.drawable.CircleDrawable;
import wang.relish.datepicker.drawable.CustomDrawable;
import wang.relish.datepicker.drawable.EndDrawable;
import wang.relish.datepicker.drawable.RectDrawable;
import wang.relish.datepicker.drawable.RingDrawable;
import wang.relish.datepicker.drawable.StartDrawable;
import wang.relish.datepicker.drawable.TodayDrawable;

/**
 * 日期范围选择器适配器
 * Created by 王鑫 on 2017/3/15.
 */
public class DatePickerAdapter extends RecyclerView.Adapter<DatePickerAdapter.VH>
        implements MonthView.OnSelectListener {

    /**
     * 日期范围选择器的日历展示范围
     */
    private int mMinYear, mMinMonth, mMinDay, mMaxYear, mMaxMonth, mMaxDay;

    /**
     * 被选择的开始日期-结束日期
     */
    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    /**
     * 现在时间
     */
    private int mNowYear, mNowMonth, mNowDay;

    /**
     * 储存所有MonthView的数据
     */
    private WeakHashMap<Integer, MonthStyle> mData;

    private boolean mIsWeekShown = true;
    private int mYearTextColor;
    private int mYearTextSize;
    private int mWeekTextColor;
    private int mWeekTextSize;
    private int mEnabledTextColor;
    private int mEnabledTextSize;
    private int mDisabledTextColor;
    private int mDisabledTextSize;

    /**
     * 日期选择改变回调监听器
     */
    private OnDateSelectedChangedListener mListener;

    /**
     * 可接受入参：<br>
     * <ul>
     * <li>0:当前年月日-1年~当前年月日</li>
     * <li>4:年月1日-年月31日</li>
     * <li>6:年月日-年月日</li>
     * </ul>
     *
     * @param dateSpan 时间范围（可接受参数个数：0，4，6）
     */
    public DatePickerAdapter(int... dateSpan) {
        mData = new WeakHashMap<>();

        Calendar calendar = Calendar.getInstance();
        mNowYear = calendar.get(Calendar.YEAR);
        mNowMonth = calendar.get(Calendar.MONTH) + 1;
        mNowDay = calendar.get(Calendar.DATE);

        int len = dateSpan.length;
        if (len == 0) {//默认无参
            mMaxYear = mNowYear;
            mMaxMonth = mNowMonth;
            mMaxDay = mNowDay;
            mMinYear = mMaxYear - 1;
            mMinMonth = mMaxMonth;
            mMinDay = mNowDay;
        } else if (len == 4) {//年月-年月
            mMinYear = dateSpan[0];
            mMinMonth = dateSpan[1];
            mMinDay = 1;
            mMaxYear = dateSpan[2];
            mMaxMonth = dateSpan[3];
            mMaxDay = Utils.getMonthDays(mMaxYear, mMaxMonth);
        } else if (len == 6) {//年月日-年月日
            mMinYear = dateSpan[0];
            mMinMonth = dateSpan[1];
            mMinDay = dateSpan[2];
            mMaxYear = dateSpan[3];
            mMaxMonth = dateSpan[4];
            mMaxDay = dateSpan[5];
        } else {
            throw new UnsupportedOperationException("暂不支持" + len + "个参数的输入");
        }
        initData();
    }

    /**
     * 初始化所有数据
     */
    public void initData() {
        int monthNum = Utils.getMonthNum(mMinYear, mMinMonth, mMaxYear, mMaxMonth);
        for (int position = 0; position < monthNum; position++) {
            initData(position);
        }
    }

    /**
     * 初始化某个MonthView的数据
     *
     * @param position 当前MonthView在RecyclerView中的位置
     */
    private void initData(int position) {
        int tempYear = mMinYear;
        int tempMonth = mMinMonth + position;
        int year = tempYear + tempMonth / 12 - (tempMonth % 12 == 0 ? 1 : 0);
        int month = tempMonth % 12 == 0 ? 12 : tempMonth % 12;
        MonthStyle monthStyle = new MonthStyle();
        monthStyle.setWeekTextColor(mWeekTextColor == 0 ? MonthStyle.WEEK_TEXT_COLOR : mWeekTextColor);
        monthStyle.setWeekTextSize(mWeekTextSize == 0 ? MonthStyle.WEEK_TEXT_SIZE : mWeekTextSize);
        monthStyle.setYearTextColor(mYearTextColor == 0 ? MonthStyle.YEAR_TEXT_COLOR : mYearTextColor);
        monthStyle.setYearTextSize(mYearTextSize == 0 ? MonthStyle.YEAR_TEXT_SIZE : mYearTextSize);
        //这个月有多少天
        int mMonthDays = Utils.getMonthDays(year, month);
        DateStyle[] items = new DateStyle[mMonthDays];
        for (int i = 0; i < mMonthDays; i++) {
            int day = i + 1;
            DateStyle item = new DateStyle(day + "");
            if (year == mMinYear && month == mMinMonth) {
                if (day < mMinDay) {//disable
                    item.setTextSize(mDisabledTextSize == 0 ? MonthStyle.DISABLE_TEXT_SIZE : mDisabledTextSize);
                    item.setTextColor(mDisabledTextColor == 0 ? MonthStyle.DISABLED_TEXT_COLOR : mDisabledTextColor);
                } else {//enable
                    item.setTextSize(mEnabledTextSize == 0 ? MonthStyle.ENABLE_TEXT_SIZE : mEnabledTextSize);
                    item.setTextColor(mEnabledTextColor == 0 ? MonthStyle.ENABLED_TEXT_COLOR : mEnabledTextColor);
                }
            } else if (year == mMaxYear && month == mMaxMonth) {
                if (day > mMaxDay) {//disable
                    item.setTextSize(mDisabledTextSize == 0 ? MonthStyle.DISABLE_TEXT_SIZE : mDisabledTextSize);
                    item.setTextColor(mDisabledTextColor == 0 ? MonthStyle.DISABLED_TEXT_COLOR : mDisabledTextColor);
                } else {//enable
                    item.setTextSize(mEnabledTextSize == 0 ? MonthStyle.ENABLE_TEXT_SIZE : mEnabledTextSize);
                    item.setTextColor(mEnabledTextColor == 0 ? MonthStyle.ENABLED_TEXT_COLOR : mEnabledTextColor);
                }
            } else {
                item.setTextSize(mEnabledTextSize == 0 ? MonthStyle.ENABLE_TEXT_SIZE : mEnabledTextSize);
                item.setTextColor(mEnabledTextColor == 0 ? MonthStyle.ENABLED_TEXT_COLOR : mEnabledTextColor);
            }
            boolean isSameYearMonth = year == mNowYear && month == mNowMonth;
            if (isSameYearMonth && mNowDay == day) {
//                item.setTextColor(MonthStyle.TODAY_TEXT_COLOR);
//                item.setText("今天");
                TodayDrawable drawable = new TodayDrawable();
                item.setDrawable(drawable);
            } else {
                item.setDrawable(null);
            }
            items[i] = item;
        }
        monthStyle.setDateStyles(items);
        mData.put(position, monthStyle);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        int m = mMinMonth + position;
        int y = mMinYear;
        int year = y + m / 12 - (m % 12 == 0 ? 1 : 0);
        int month = m % 12 == 0 ? 12 : m % 12;
        holder.monthView.setYearMonth(year, month);
        if (mData.get(position) == null) {
            initData(position);
        }
        holder.monthView.setMonthStyle(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return Utils.getMonthNum(mMinYear, mMinMonth, mMaxYear, mMaxMonth);
    }

    class VH extends RecyclerView.ViewHolder {

        MonthView monthView;

        private VH(View itemView) {
            super(itemView);
            monthView = (MonthView) itemView;
            monthView.setIsWeekShown(mIsWeekShown);
            monthView.setDateRange(mMinYear, mMinMonth, mMinDay, mMaxYear, mMaxMonth, mMaxDay);
            monthView.setSelectListener(DatePickerAdapter.this);
        }
    }

    /**
     * MonthView日期选择后回调处
     *
     * @param monthView 被点击的MonthView
     * @param year      年份
     * @param month     月份
     * @param day       日期
     */
    @Override
    public void onSelect(MonthView monthView, int year, int month, int day) {
        if (mStartYear == 0 && mStartMonth == 0 && mStartDay == 0) {
            setSingleDate(year, month, day);
        } else {
            if (mEndYear == 0 && mEndMonth == 0 && mEndDay == 0 && isBigThenBefore(year, month, day)) {
                setDoubleDate(year, month, day);
            } else {
                if (mStartYear == year && mStartMonth == month && mStartDay == day
                        && (mEndYear == year && mEndMonth == month && mEndDay == day
                        || mEndYear == 0 && mEndMonth == 0 && mEndDay == 0)) {
                    setSingleDateDoubleClicked(year, month, day);
                } else {
                    setSingleDate(year, month, day);
                }
            }
        }
    }

    /**
     * 双击一个日期
     *
     * @param year  开始/终止年份
     * @param month 开始/终止月份
     * @param day   开始/终止日期
     */
    private void setSingleDateDoubleClicked(int year, int month, int day) {
        if (mListener != null) {
            //清除所有样式,再设置头样式
            clearAllItemStyle();
            mStartYear = year;
            mStartMonth = month;
            mStartDay = day;
            mEndYear = year;
            mEndMonth = month;
            mEndDay = day;
            setItemAsDoubleSelected();
            //显示选择结果
            mListener.onSelectedChanged(mStartYear, mStartMonth, mStartDay,
                    mEndYear, mEndMonth, mEndDay);
        } else {
            throw new UnsupportedOperationException("did you forget to set callback?");
        }
    }

    /**
     * 点击一个日期
     * 1 传入
     * 2 点击
     *
     * @param year  开始/终止年份
     * @param month 开始/终止月份
     * @param day   开始/终止日期
     */
    private void setSingleDate(int year, int month, int day) {
        if (mListener != null) {
            //清除所有样式,再设置头样式
            clearAllItemStyle();
            mStartYear = year;
            mStartMonth = month;
            mStartDay = day;
            setItemAsSingleStart();
            //显示选择结果
            mListener.onSelectedChanged(mStartYear, mStartMonth, mStartDay);
            //重置结束日期
            mEndYear = 0;
            mEndDay = 0;
            mEndMonth = 0;
        } else {
            throw new UnsupportedOperationException("did you forget to set callback?");
        }
    }

    /**
     * 选择一个时间区间
     *
     * @param year  终止年份
     * @param month 终止月份
     * @param day   终止日期
     */
    private void setDoubleDate(int year, int month, int day) {
        if (mListener != null) {
            //1 先清除样式
            clearAllItemStyleAndResetStartDate();
            mEndYear = year;
            mEndMonth = month;
            mEndDay = day;
            //2 再设置样式
            //2.1 范围样式(顺序很重要,范围样式会被设置到头尾样式上，然后再设置到头尾样式用以替换)
            setItemsAsWithinRange();
            //2.2 头样式
            setItemAsStartOfRange();
            //2.3 尾样式
            setItemAsEndOfRange();

            mListener.onSelectedChanged(mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay);
        } else {
            throw new UnsupportedOperationException("did you forget to set callback?");
        }
    }

    /**
     * 设置开始/终止同一天的圆环样式
     */
    private void setItemAsDoubleSelected() {
        CustomDrawable drawable = new RingDrawable();
        int position = getPosition(mStartYear, mStartMonth);
        mData.get(position).getDateStyles()[mStartDay - 1].setDrawable(drawable);//index of List is started with 0
        mData.get(position).getDateStyles()[mStartDay - 1].setTextColor(MonthStyle.ENDS_TEXT_COLOR);//index of List is started with 0
    }

    /**
     * 设置开始/结束日期的小圆点样式
     */
    private void setItemAsSingleStart() {
        CustomDrawable drawable = new CircleDrawable();
        int position = getPosition(mStartYear, mStartMonth);
        mData.get(position).getDateStyles()[mStartDay - 1].setDrawable(drawable);//index of List is started with 0
        mData.get(position).getDateStyles()[mStartDay - 1].setTextColor(MonthStyle.ENDS_TEXT_COLOR);//index of List is started with 0
    }

    /**
     * 选定范围的开始日期样式
     */
    private void setItemAsStartOfRange() {
        CustomDrawable drawable = new StartDrawable();
        int position = getPosition(mStartYear, mStartMonth);
        mData.get(position).getDateStyles()[mStartDay - 1].setDrawable(drawable);//index of List is started with 0
        mData.get(position).getDateStyles()[mStartDay - 1].setTextColor(MonthStyle.ENDS_TEXT_COLOR);//index of List is started with 0
    }

    /**
     * 选定范围后的终止日期样式
     */
    private void setItemAsEndOfRange() {
        CustomDrawable drawable = new EndDrawable();
        int position = getPosition(mEndYear, mEndMonth);
        mData.get(position).getDateStyles()[mEndDay - 1].setDrawable(drawable);//index of List is started with 0
        mData.get(position).getDateStyles()[mEndDay - 1].setTextColor(MonthStyle.ENDS_TEXT_COLOR);//index of List is started with 0
    }

    /**
     * 设置范围内样式
     */
    private void setItemsAsWithinRange() {
        if (mStartYear == mEndYear) {
            if (mStartMonth == mEndMonth) {
                setItemStyleOfWithinRange(mStartYear, mStartMonth, mStartDay, mEndDay);
            } else {
                //开始月
                setItemStyleOfWithinRangeMonthTail(mStartYear, mStartMonth, mStartDay);
                //范围月
                for (int m = mStartMonth + 1; m < mEndMonth; m++) {
                    setItemStyleOfWithinRange(mStartYear, m);
                }
                //结束月
                setItemStyleOfWithinRangeMonthHead(mEndYear, mEndMonth, mEndDay);
            }
        } else {
            //开始月
            setItemStyleOfWithinRangeMonthTail(mStartYear, mStartMonth, mStartDay);
            if (mStartMonth < 12) {
                //开始年
                for (int m = mStartMonth + 1; m <= 12; m++) {
                    setItemStyleOfWithinRange(mStartYear, m);
                }
            }
            //范围年
            for (int y = mStartYear + 1; y < mEndYear; y++) {
                for (int m = 1; m <= 12; m++) {
                    setItemStyleOfWithinRange(y, m);
                }
            }
            //结束年
            if (mEndMonth > 1) {
                for (int m = 1; m < mEndMonth; m++) {
                    setItemStyleOfWithinRange(mEndYear, m);
                }
            }
            //结束月
            setItemStyleOfWithinRangeMonthHead(mEndYear, mEndMonth, mEndDay);
        }
    }

    /**
     * 设置整月的Item样式为选定日期范围内的样式
     *
     * @param year  年份
     * @param month 月份
     */
    private void setItemStyleOfWithinRange(int year, int month) {
        int endDay = Utils.getMonthDays(year, month);
        setItemStyleOfWithinRange(year, month, 1, endDay);//the first day of a month is 1
    }

    /**
     * 设置当月从月初开始到{@param endDay}结束期间的Item样式为选定日期范围内的样式
     *
     * @param year   年份
     * @param month  月份
     * @param endDay 终止日期
     */
    private void setItemStyleOfWithinRangeMonthHead(int year, int month, int endDay) {
        setItemStyleOfWithinRange(year, month, 1, endDay);//the first day of a month is 1
    }

    /**
     * 设置当月从{@param startDay}开始到月末期间的Item样式为选定日期范围内的样式
     *
     * @param year     年份
     * @param month    月份
     * @param startDay 开始日期
     */
    private void setItemStyleOfWithinRangeMonthTail(int year, int month, int startDay) {
        int endDay = Utils.getMonthDays(year, month);
        setItemStyleOfWithinRange(year, month, startDay, endDay);
    }

    /**
     * 设置当月从{@param startDay}开始到{@param endDay}结束期间的Item样式为选定日期范围内的样式
     *
     * @param year     年份
     * @param month    月份
     * @param startDay 开始日期
     * @param endDay   终止日期
     */
    private void setItemStyleOfWithinRange(int year, int month, int startDay, int endDay) {
        if (startDay == endDay) {
            return;
        }
        RectDrawable drawable = new RectDrawable();
        int position = getPosition(year, month);
        for (int day = startDay; day <= endDay; day++) {
            //day-1:下标从0开始
            mData.get(position).getDateStyles()[day - 1].setDrawable(drawable);//the index of List is started with 0
            mData.get(position).getDateStyles()[day - 1].setTextColor(MonthStyle.WITH_RANGE_TEXT_COLOR);//the index of List is started with 0
        }
    }

    /**
     * 清除所有样式
     */
    private void clearAllItemStyleAndResetStartDate() {
        clearAllItemStyle();//清除之前所有样式
        setItemAsSingleStart();//重新设置开始日期
    }

    /**
     * 清除所有样式（变成未选择状态）
     */
    private void clearAllItemStyle() {
        if (mStartYear == 0 && mStartMonth == 0 && mStartDay == 0) {
            return;
        }
        if (mEndYear == 0 && mEndMonth == 0 && mEndDay == 0) {//表示未选择过结束日期
            //只需要清除开始日期的样式
            clearItemStyle(mStartYear, mStartMonth, mStartDay, mStartDay);
            return;
        }
        if (mStartYear == mEndYear) {//同一年
            //开始月
            clearItemStyleTail(mStartYear, mStartMonth, mStartDay);
            //范围月
            for (int m = mStartMonth + 1; m < mEndMonth; m++) {
                clearItemStyle(mStartYear, m);
            }
            //结束月
            clearItemStyleHead(mEndYear, mEndMonth, mEndDay);
        } else {
            //开始月
            clearItemStyleTail(mStartYear, mStartMonth, mStartDay);
            //开始年
            for (int m = mStartMonth + 1; m <= 12; m++) {
                clearItemStyle(mStartYear, m);
            }
            //范围年
            for (int y = mStartYear + 1; y < mEndYear; y++) {
                for (int m = 1; m < 12; m++) {
                    clearItemStyle(y, m);
                }
            }
            //结束年
            for (int m = 1; m < mEndMonth; m++) {
                clearItemStyle(mEndYear, m);
            }
            //结束月
            clearItemStyleHead(mEndYear, mEndMonth, mEndDay);
        }
    }


    /**
     * 清除整月Item样式
     *
     * @param year  年份
     * @param month 月份
     */
    private void clearItemStyle(int year, int month) {
        int endDay = Utils.getMonthDays(year, month);
        clearItemStyle(year, month, 1, endDay);
    }


    /**
     * 清除当月从{@param startDay}开始到月末结束期间的Item样式
     *
     * @param year     年份
     * @param month    月份
     * @param startDay 开始日期
     */
    private void clearItemStyleTail(int year, int month, int startDay) {
        int endDay = Utils.getMonthDays(year, month);
        clearItemStyle(year, month, startDay, endDay);
    }

    /**
     * 清除当月从月初开始到{@param endDay}结束期间的Item样式
     *
     * @param year   年份
     * @param month  月份
     * @param endDay 结束日期
     */
    private void clearItemStyleHead(int year, int month, int endDay) {
        clearItemStyle(year, month, 1, endDay);
    }

    /**
     * 清理当月从{@param startDay}开始到{@param endDay}结束期间的Item样式
     *
     * @param year     年份
     * @param month    月份
     * @param startDay 开始日期
     * @param endDay   结束日期
     */
    private void clearItemStyle(int year, int month, int startDay, int endDay) {
        Calendar calendar = Calendar.getInstance();
        int mNowYear = calendar.get(Calendar.YEAR);
        int mNowMonth = calendar.get(Calendar.MONTH) + 1;
        int mNowDay = calendar.get(Calendar.DAY_OF_MONTH);

        int position = getPosition(year, month);
        for (int day = startDay; day <= endDay; day++) {
            mData.get(position).getDateStyles()[day - 1].setDrawable(null);
            if (year == mMinYear && month == mMinMonth) {
                boolean enable = day >= mMinDay;
                int color = enable ?
                        (mEnabledTextColor == 0 ? MonthStyle.ENABLED_TEXT_COLOR : mEnabledTextColor) :
                        (mDisabledTextColor == 0 ? MonthStyle.DISABLED_TEXT_COLOR : mDisabledTextColor);
                mData.get(position).getDateStyles()[day - 1].setTextColor(color);
            } else if (year == mMaxYear && month == mMaxMonth) {
                boolean enable = day <= mMaxDay;
                int color = enable ?
                        (mEnabledTextColor == 0 ? MonthStyle.ENABLED_TEXT_COLOR : mEnabledTextColor) :
                        (mDisabledTextColor == 0 ? MonthStyle.DISABLED_TEXT_COLOR : mDisabledTextColor);
                mData.get(position).getDateStyles()[day - 1].setTextColor(color);
            } else {
                mData.get(position).getDateStyles()[day - 1].setTextColor(
                        mEnabledTextColor == 0 ? MonthStyle.ENABLED_TEXT_COLOR : mEnabledTextColor);
            }
            boolean isSameYearMonth = year == mNowYear && month == mNowMonth;
            if (isSameYearMonth && mNowDay == day) {
                TodayDrawable drawable = new TodayDrawable();
                mData.get(position).getDateStyles()[day - 1].setDrawable(drawable);
//                mData.get(position).getDateStyles()[day-1].setTextColor(MonthStyle.TODAY_TEXT_COLOR);
            }
        }
    }

    /**
     * 设置DataPicker的时间选择范围
     *
     * @param dateSpan 日期区间
     */
    public void setDateRange(int... dateSpan) {
        int len = dateSpan.length;
        if (len == 4) {
            mMinYear = dateSpan[0];
            mMinMonth = dateSpan[1];
            mMinDay = 1;
            mMaxYear = dateSpan[2];
            mMaxMonth = dateSpan[3];
            mMaxDay = Utils.getMonthDays(mMaxYear, mMaxMonth);
            initData();
        } else if (len == 6) {
            mMinYear = dateSpan[0];
            mMinMonth = dateSpan[1];
            mMinDay = dateSpan[2];
            mMaxYear = dateSpan[3];
            mMaxMonth = dateSpan[4];
            mMaxDay = dateSpan[5];
            initData();
        } else {
            throw new UnsupportedOperationException("暂不支持" + len + "个参数的输入");
        }
    }

    /**
     * 终止时间是否大于开始时间（不包含边界）
     *
     * @param y 假定的终止时间年份
     * @param m 假定的终止时间月份
     * @param d 假定的终止时间日期
     * @return 是否大于
     */
    private boolean isBigThenBefore(int y, int m, int d) {
        return y > mStartYear ||
                y >= mStartYear &&
                        (m > mStartMonth ||
                                m >= mStartMonth && d > mStartDay);
    }

    /**
     * 根据年月计算MonthView在RecyclerView中的位置(position)
     *
     * @param y 年份
     * @param m 月份
     * @return 在RecyclerView中的位置(从0开始)
     */
    private int getPosition(int y, int m) {
        if (y == mMinYear) {
            return m - mMinMonth;
        } else {
            int position = 0;
            position += 12 - mMinMonth;
            position += (y - mMinYear - 1) * 12;
            position += m;
            return position;
        }
    }

    public static int getPosition(int minYear, int minMonth, int maxYear, int maxMonth, int y, int m) {
        if (y == minYear) {
            return m - minMonth;
        } else {
            int position = 0;
            position += 12 - minMonth;
            position += (y - minYear - 1) * 12;
            position += m;
            return position;
        }
    }

    /**
     * 从上级页面传进来的
     *
     * @param startYear  开始年份
     * @param startMonth 开始月份
     * @param startDay   开始日期
     * @param endYear    终止年份
     * @param endMonth   终止月份
     * @param endDay     终止日期
     */
    public void setSelectedDate(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        mStartYear = startYear;
        mStartMonth = startMonth;
        mStartDay = startDay;
        mEndYear = endYear;
        mEndMonth = endMonth;
        mEndDay = endDay;
        if (!(mStartYear == 0 && mStartMonth == 0 && mStartDay == 0)) {
            if (mEndYear == 0 && mEndMonth == 0 && mEndDay == 0 ||
                    mStartYear == mEndYear && mStartMonth == mEndMonth && mStartDay == mEndDay) {
                setItemAsDoubleSelected();
                //显示选择结果
                if (mListener != null) {
                    if (mStartYear == mEndYear && mStartMonth == mEndMonth && mStartDay == mEndDay) {
                        mListener.onSelectedChanged(mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay);
                    } else {
                        mListener.onSelectedChanged(mStartYear, mStartMonth, mStartDay);
                    }
                }
            } else {
                //2 再设置样式
                //2.1 范围样式(顺序很重要,范围样式会被设置到头尾样式上，然后再设置到头尾样式用以替换)
                setItemsAsWithinRange();
                //2.2 头样式
                setItemAsStartOfRange();
                //2.3 尾样式
                setItemAsEndOfRange();

                if (mListener != null) {
                    mListener.onSelectedChanged(mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay);
                }
            }
        }
    }

    /**
     * 设置MonthView中星期行是否显示
     *
     * @param isWeekShown 是否显示星期行
     */
    public void setWeekShownInMonthView(boolean isWeekShown) {
        mIsWeekShown = isWeekShown;
    }

    /**
     * 设置MonthView中显示年月的文字的颜色
     *
     * @param mYearTextColor 显示XXXX年XX月的文字的颜色
     */
    public void setYearTextColor(int mYearTextColor) {
        this.mYearTextColor = mYearTextColor;
        initData();
    }

    /**
     * 设置MonthView中显示年月的文字的大小
     *
     * @param mYearTextSize 显示XXXX年XX月的文字的大小
     */
    public void setYearTextSize(int mYearTextSize) {
        this.mYearTextSize = mYearTextSize;
        initData();
    }

    /**
     * 设置MonthView中显示周日~周六的文字的颜色
     *
     * @param mWeekTextColor 显示周日~周六的文字的颜色
     */
    public void setWeekTextColor(int mWeekTextColor) {
        this.mWeekTextColor = mWeekTextColor;
        initData();
    }

    /**
     * 设置MonthView中显示周日~周六的文字的大小
     *
     * @param mWeekTextSize 显示周日~周六的文字的大小
     */
    public void setWeekTextSize(int mWeekTextSize) {
        this.mWeekTextSize = mWeekTextSize;
        initData();
    }

    /**
     * 设置MonthView中可以被选择的日期的文字的颜色
     *
     * @param mEnabledTextColor 可以被选择的日期的文字的颜色
     */
    public void setEnabledTextColor(int mEnabledTextColor) {
        this.mEnabledTextColor = mEnabledTextColor;
        initData();
    }

    /**
     * 设置MonthView中可以被选择的日期的文字的大小
     *
     * @param mEnabledTextSize 可以被选择的日期的文字的大小
     */
    public void setEnabledTextSize(int mEnabledTextSize) {
        this.mEnabledTextSize = mEnabledTextSize;
        initData();
    }

    /**
     * 设置MonthView中不可被选择的日期的文字的颜色
     *
     * @param mDisabledTextColor 不可被选择的日期的文字的颜色
     */
    public void setDisabledTextColor(int mDisabledTextColor) {
        this.mDisabledTextColor = mDisabledTextColor;
        initData();
    }

    /**
     * 设置MonthView中不可被选择的日期的文字的大小
     *
     * @param mDisabledTextSize 不可被选择的日期的文字的大小
     */
    public void setDisabledTextSize(int mDisabledTextSize) {
        this.mDisabledTextSize = mDisabledTextSize;
        initData();
    }

    /**
     * 设置时间选择改变回调监听器
     *
     * @param listener 时间选择改变回调监听器
     */
    public void setOnSelectedChangedListener(OnDateSelectedChangedListener listener) {
        mListener = listener;
    }

    /**
     * 时间选择改变回调监听器
     */
    public interface OnDateSelectedChangedListener {
        /**
         * 时间变化回调
         *
         * @param daySpan 时间区间
         */
        void onSelectedChanged(int... daySpan);
    }
}
