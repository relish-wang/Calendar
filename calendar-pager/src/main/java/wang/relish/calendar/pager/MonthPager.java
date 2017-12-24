package wang.relish.calendar.pager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import wang.relish.calendar.MonthView;
import wang.relish.calendar.pager.viewpager.RecyclerViewPager;


/**
 * 日历的ViewPager
 *
 * @author Relish Wang
 * @since 2017/11/28
 */
public class MonthPager extends RecyclerViewPager implements ITopView {

    public MonthPager(@NonNull Context context) {
        super(context);
    }

    public MonthPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private int mItemHeight = -1;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int monthHeight = 0;
        if (getAdapter() != null) {
            ITopView view = (ITopView) getChildAt(0);
            if (view != null) {
                monthHeight = view.getMeasuredHeight();
                mItemHeight = view.getItemHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(monthHeight, View.MeasureSpec.EXACTLY));
    }

    private MonthPagerAdapter mAdapter;

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof MonthPagerAdapter) {
            mAdapter = (MonthPagerAdapter) adapter;
            if (mListener != null) mAdapter.setOnTopViewChangedListener(mListener);
        } else {
            throw new IllegalArgumentException("MonthPager must set a MonthPagerAdapter: " + adapter);
        }
        super.setAdapter(mAdapter);
    }

    @Override
    public int getItemTop() {
        View view = getChildAt(0);
        if (view != null && view instanceof MonthView) {
            PMonthView monthView = (PMonthView) view;
            return monthView.getItemTop();
        }
        return 0;
    }

    @Override
    public int getItemHeight() {
        if (mItemHeight != -1) return mItemHeight;
        View view = getChildAt(getCurrentPosition());
        if (view != null && view instanceof PMonthView) {
            PMonthView monthView = (PMonthView) view;
            mItemHeight = monthView.getItemHeight();
            return mItemHeight;
        }
        return 0;
    }

    private OnTopViewChangedListener mListener;

    @Override
    public void setOnTopViewChangedListener(OnTopViewChangedListener listener) {
        mListener = listener;
    }

    @Override
    public View getView() {
        return this;
    }
}
