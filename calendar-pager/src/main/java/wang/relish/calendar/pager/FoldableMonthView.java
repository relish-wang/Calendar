package wang.relish.calendar.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import wang.relish.calendar.MonthAdapter;
import wang.relish.calendar.MonthView;

/**
 * @author Relish Wang
 * @since 2017/12/23
 */
public class FoldableMonthView extends MonthView implements ITopView {

    public FoldableMonthView(Context context) {
        super(context);
    }

    public FoldableMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getItemTop() {
        int currSelectedPosition = Utils.getPositionOfDateInMonthView(getMonthStyle());
        int selectedLine = currSelectedPosition / 7;
        return (int) (selectedLine * mCellHeight);
    }

    @Override
    public int getItemHeight() {
        return (int) mCellHeight;
    }

    @Override
    public void setAdapter(MonthAdapter adapter) {
        super.setAdapter(adapter);
        if (mOnTopViewChangedListener != null) {
            mOnTopViewChangedListener.onLayoutChanged(this);
        }
    }

    private OnTopViewChangedListener mOnTopViewChangedListener;

    @Override
    public void setOnTopViewChangedListener(OnTopViewChangedListener listener) {
        mOnTopViewChangedListener = listener;
    }

    @Override
    public View getView() {
        return this;
    }
}
