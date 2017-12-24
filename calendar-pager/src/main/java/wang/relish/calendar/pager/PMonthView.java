package wang.relish.calendar.pager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import wang.relish.calendar.DateStyle;
import wang.relish.calendar.IDrawable;
import wang.relish.calendar.MonthStyle;
import wang.relish.calendar.MonthView;

/**
 * @author Relish Wang
 * @since 2017/12/23
 */
public class PMonthView extends MonthView implements ITopView {

    public PMonthView(Context context) {
        super(context);
    }

    public PMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onDrawCell(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle) {
        PDateStyle pStyle;
        if (dateStyle instanceof PDateStyle) {
            pStyle = (PDateStyle) dateStyle;
        } else {
            pStyle = PDateStyle.toPDateStyle(dateStyle);
        }
        super.onDrawCell(canvas, cell, dateStyle);
        IDrawable badgeDrawable = pStyle.getBadgeDrawable();
        if (badgeDrawable != null) badgeDrawable.draw(canvas, cell);
    }

    @Override
    public int getItemTop() {
        int currSelectedPosition = wang.relish.calendar.Utils.getPositionOfDateInMonthView(mMonthStyle);
        int selectedLine = currSelectedPosition / 7;
        return (int) (selectedLine * mCellHeight);
    }

    @Override
    public int getItemHeight() {
        return (int) mCellHeight;
    }


    @Override
    public void setMonthStyle(MonthStyle monthStyle) {
        super.setMonthStyle(monthStyle);
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
