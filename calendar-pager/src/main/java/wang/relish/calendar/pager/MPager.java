package wang.relish.calendar.pager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Relish Wang
 * @since 2018/01/21
 */
public class MPager extends ViewPager implements ITopView {

    public MPager(@NonNull Context context) {
        super(context);
    }

    public MPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private MPagerAdapter mAdapter;


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
    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        if (adapter instanceof MPagerAdapter) {
            mAdapter = (MPagerAdapter) adapter;
            if (mListener != null) mAdapter.setOnTopViewChangedListener(mListener);
        } else {
            throw new IllegalArgumentException("MonthPager must set a MPagerAdapter: " + adapter);
        }
        super.setAdapter(mAdapter);
    }

    @Override
    public int getItemTop() {
        View view = getChildAt(0);
        if (view != null && view instanceof FoldableMonthView) {
            FoldableMonthView monthView = (FoldableMonthView) view;
            return monthView.getItemTop();
        }
        return 0;
    }

    @Override
    public int getItemHeight() {
        if (mItemHeight != -1) return mItemHeight;
        View view = getChildAt(getCurrentItem());
        if (view != null && view instanceof FoldableMonthView) {
            FoldableMonthView monthView = (FoldableMonthView) view;
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
