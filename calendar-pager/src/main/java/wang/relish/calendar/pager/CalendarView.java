package wang.relish.calendar.pager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import wang.relish.calendar.MonthView;
import wang.relish.calendar.OnSelectListener;
import wang.relish.calendar.WeekView;
import wang.relish.calendar.pager.listener.OnChangeDateListener;
import wang.relish.calendar.pager.listener.OnChangePageListener;
import wang.relish.calendar.pager.listener.OnChangeStatusListener;
import wang.relish.calendar.pager.listener.OnClickBackTodayListener;
import wang.relish.calendar.pager.viewpager.RecyclerViewPager;

//import wang.relish.calendar.Utils;


/**
 * 日历
 *
 * @author Relish Wang
 * @since 2017/11/20
 */
public class CalendarView extends LinearLayout {

    public CalendarView(@NonNull Context context) {
        this(context, null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context, attrs, defStyleAttr);
    }

    private LinearLayout mLlRoot;
    protected CalendarHeaderView mCalendarHeader;

    protected WeekView mWeekView;

    private CalendarLayout mCalendarLayout;

    /**
     * 数据
     */
    private Map<String, Integer> mData = new HashMap<>();
    private MonthPagerAdapter mMonthAdapter;
    /**
     * 展示月份的ViewPager
     */
    private MonthPager mMonthPager;

    protected FrameLayout mContentRootLayout;

    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mNowYear, mNowMonth, mNowDay;

    /**
     * 设定一周的第一天是周几
     * (默认周一)
     */
    private int mWeekFirstDay = Calendar.MONDAY;

    /**
     * 是否是手动滑动的
     */
    private boolean isManuallySlided = true;
    private int currentPosition = Integer.MAX_VALUE >> 1;
    private int stage = Stage.FOLD;

    @SuppressLint("RestrictedApi")
    private void initViews(final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        Calendar now = Calendar.getInstance();
        mNowYear = now.get(Calendar.YEAR);
        mNowMonth = now.get(Calendar.MONTH);
        mNowDay = now.get(Calendar.DAY_OF_MONTH);

        mCurrYear = mNowYear;
        mCurrMonth = mNowMonth;
        mCurrDay = mNowDay;

        LayoutInflater inflater = LayoutInflater.from(context);
        mLlRoot = (LinearLayout) inflater.inflate(R.layout.pagerlib_calendar, this, false);
        mCalendarHeader = mLlRoot.findViewById(R.id.calendar_header);
        mWeekView = mLlRoot.findViewById(R.id.week_view);
        mCalendarHeader.setOnClickBackTodayListener(new OnClickBackTodayListener() {
            @Override
            public void onClickBackToday() {
                goToday();
            }
        });
        mCalendarLayout = mLlRoot.findViewById(R.id.calendar_layout);
        mContentRootLayout = mLlRoot.findViewById(R.id.rl_content);

        mMonthPager = mLlRoot.findViewById(R.id.vp_month);
        mMonthAdapter = new MonthPagerAdapter(mData, new OnSelectListener() {
            @Override
            public void onPrevMonthDateSelect(MonthView monthView, int year, int month, int day) {
                if (mCurrYear == year && mCurrMonth == month && mCurrDay == day) return;
                mCurrYear = year;
                mCurrMonth = month;
                mCurrDay = day;
                onChangeDate(getContext(), mCurrYear, mCurrMonth, mCurrDay);

                final int currentItem = mMonthPager.getCurrentPosition();
                mMonthPager.post(new Runnable() {
                    @Override
                    public void run() {
                        isManuallySlided = false;
                        mMonthPager.smoothScrollToPosition(currentItem - 1);
                    }
                });
            }

            @Override
            public void onCurrMonthDateSelect(MonthView monthView, int year, int month, int day) {
                if (mCurrYear == year && mCurrMonth == month && mCurrDay == day) return;
                mCurrYear = year;
                mCurrMonth = month;
                mCurrDay = day;
                mMonthAdapter.selectDate(year, month, day);

                onChangeDate(getContext(), year, month, day);
            }

            @Override
            public void onNextMonthDateSelect(MonthView monthView, int year, int month, int day) {
                if (mCurrYear == year && mCurrMonth == month && mCurrDay == day) return;
                mCurrYear = year;
                mCurrMonth = month;
                mCurrDay = day;

                onChangeDate(getContext(), year, month, day);

                final int currentItem = mMonthPager.getCurrentPosition();
                mMonthPager.post(new Runnable() {
                    @Override
                    public void run() {
                        isManuallySlided = false;
                        mMonthPager.smoothScrollToPosition(currentItem + 1);
                    }
                });
            }
        }, new MonthPagerAdapter.CurrentPositionGetter() {

            @Override
            public int getCurrentPosition() {
                return currentPosition;
            }

            @Override
            public int getCurrentStage() {
                return mCalendarLayout.getStage();
            }

            @Override
            public int getWeekFirstDay() {
                return mWeekFirstDay;
            }
        });
        mMonthPager.setAdapter(mMonthAdapter);
        mMonthPager.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mMonthPager.scrollToPosition(Integer.MAX_VALUE >> 1);
        mMonthPager.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {

            @Override
            public void onPageChanged(int oldPosition, int newPosition) {
                if (oldPosition == newPosition) return;
                currentPosition = newPosition;
                if (oldPosition == 0) return;// 说明是第一次进来
                @Dir int dir = oldPosition - newPosition > 0
                        ? Dir.LEFT :
                        Dir.RIGHT;
                stage = mCalendarLayout.getStage();

                if (isManuallySlided) {
                    //手指滑动
                    Calendar nextFocusDate = Utils.getNextFocusDate(dir, stage, mCurrYear, mCurrMonth, mCurrDay, mWeekFirstDay);
                    mCurrYear = nextFocusDate.get(Calendar.YEAR);
                    mCurrMonth = nextFocusDate.get(Calendar.MONTH);
                    mCurrDay = nextFocusDate.get(Calendar.DAY_OF_MONTH);
                    onChangeDate(getContext(), mCurrYear, mCurrMonth, mCurrDay);
                } else {
                    isManuallySlided = true;
                }
                if (mOnChangePageListener != null) {
                    mOnChangePageListener.onChangePage(getContext(), mCurrYear, mCurrMonth, mCurrDay);
                }
                mMonthPager.post(new Runnable() {
                    @Override
                    public void run() {
                        mMonthAdapter.selectDate(mCurrYear, mCurrMonth, mCurrDay);
                    }
                });
            }
        });

        View contentLayout = null;
        // Custom attributes
        @SuppressLint("RestrictedApi") TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.CalendarView, defStyleAttr, 0);

        if (a.hasValue(R.styleable.CalendarView_contentLayout)) {
            @LayoutRes int layoutResId = a.getResourceId(R.styleable.CalendarView_contentLayout, 0);
            contentLayout = inflater.inflate(layoutResId, mContentRootLayout, false);
        }
        if (a.hasValue(R.styleable.CalendarView_weekFirstDay)) {
            mWeekFirstDay = a.getInt(R.styleable.CalendarView_weekFirstDay, mWeekFirstDay);
        }
        a.recycle();

        setContentLayout(contentLayout);
        super.addView(mLlRoot);
        onChangeDate(getContext(), mCurrYear, mCurrMonth, mCurrDay);
    }

    @Override
    public void addView(View child) {
        mContentRootLayout.addView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        mContentRootLayout.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        mContentRootLayout.addView(child, width, height);
    }

    /**
     * 设置外部布局
     *
     * @param contentLayout 外部传入的布局
     */
    public void setContentLayout(@Nullable View contentLayout) {
        if (mContentRootLayout == null) {
            if (BuildConfig.DEBUG) Logger.d("mContentRootLayout == null!!");
            return;
        }
        if (contentLayout == null) return;
        Drawable background = mContentRootLayout.getBackground();
        if (background == null) {
            mContentRootLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }
        mContentRootLayout.addView(contentLayout);
    }

    private final Runnable mLayoutRunnable = new Runnable() {
        @Override
        public void run() {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    @Override
    public void requestLayout() {
        super.requestLayout();

        // The toolbar relies on a measure + layout pass happening after it calls requestLayout().
        // Without this, certain calls (e.g. setLogo) only take effect after a second invalidation.
        post(mLayoutRunnable);
    }

    protected void goToday() {
        selectDate(mNowYear, mNowMonth, mNowDay);
    }

    protected void selectDate(int year, int month, int day) {
        // 埋点：CRM_APP_TASK_BACKTODAY
        if (mClickBackTodayListener != null) {
            mClickBackTodayListener.onClickBackToday();//点了就回调, 就在今天你点了也给你回调
        }
        if (this.mCurrYear == year && this.mCurrMonth == month && this.mCurrDay == day) {
            // 哪怕没改变日期也要回调（业务那边非要我回调）
            onChangeDate(getContext(), mCurrYear, mCurrMonth, mCurrDay);
            return;
        }

        final boolean isFuture = Utils.isFuture(year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.mCurrYear, this.mCurrMonth, this.mCurrDay, 0, 0, 0);
        this.mCurrYear = year;
        this.mCurrMonth = month;
        this.mCurrDay = day;

        stage = mCalendarLayout.getStage();

        boolean isCurrentPage = Utils.isWithinMonthViewPage(calendar, stage, mCurrYear, mCurrMonth, mCurrDay, mWeekFirstDay);

        final int currentPosition = mMonthPager.getCurrentPosition();
        if (!isCurrentPage) {
            mMonthPager.post(new Runnable() {
                @Override
                public void run() {
                    isManuallySlided = false;
                    if (isFuture) {
                        mMonthPager.smoothScrollToPosition(currentPosition - 2);
                    } else {
                        mMonthPager.smoothScrollToPosition(currentPosition + 2);
                    }
                    onChangeDate(getContext(), mCurrYear, mCurrMonth, mCurrDay);
                }
            });
        } else {
            mMonthAdapter.selectDate(mCurrYear, mCurrMonth, mCurrDay);
            onChangeDate(getContext(), mCurrYear, mCurrMonth, mCurrDay);
        }
    }

    public void setData(Map<String, Integer> data) {
        mData = data;
        mMonthAdapter.setNewData(mData);
        invalidate();
    }

    public void setWeekFirstDay(int weekFirstDay) {
        mWeekFirstDay = weekFirstDay;
        invalidate();
    }

    //以下RN相关

    private OnClickBackTodayListener mClickBackTodayListener;
    private OnChangeDateListener mDateListener;
    private OnChangePageListener mOnChangePageListener;
    private OnChangeStatusListener mChangeStatusListener;

    public void setOnClickBackTodayListener(OnClickBackTodayListener listener) {
        mClickBackTodayListener = listener;
    }

    public void setOnChangeDateListener(OnChangeDateListener listener) {
        mDateListener = listener;
    }

    protected void setOnChangePageListener(OnChangePageListener listener) {
        mOnChangePageListener = listener;
    }

    public void setOnChangeStatusListener(OnChangeStatusListener listener) {
        this.mChangeStatusListener = listener;
        mCalendarLayout.setOnChangeStatusListener(mChangeStatusListener);
    }

    public void onChangeDate(Context context, int year, int month, int day) {
        if (mCalendarHeader != null) {
            mCalendarHeader.onChangeDate(context, year, month, day);
        }
        if (mDateListener != null) {
            mDateListener.onChangeDate(context, year, month + 1, day);
        }
    }
}
