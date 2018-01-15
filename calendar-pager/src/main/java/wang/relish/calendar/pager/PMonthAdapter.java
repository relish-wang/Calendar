package wang.relish.calendar.pager;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import wang.relish.calendar.DateStyle;
import wang.relish.calendar.IDrawable;
import wang.relish.calendar.MonthAdapter;
import wang.relish.calendar.MonthStyle;

/**
 * @author Relish Wang
 * @since 2017/12/26
 */
public class PMonthAdapter extends MonthAdapter {

    PMonthAdapter(MonthStyle monthStyle) {
        super(monthStyle);
    }

    @Override
    protected void drawUnderDate(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle) {
        // 1 选中样式
        IDrawable activeDrawable = dateStyle.getDrawable(Constant.KEY_ACTIVE_DRAWABLE);
        if (activeDrawable != null) activeDrawable.draw(canvas, cell);
    }

    @Override
    protected void drawAboveDate(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle) {
        // 3 徽标
        IDrawable badgeDrawable = dateStyle.getDrawable(Constant.KEY_BADGE_DRAWABLE);
        if (badgeDrawable != null) badgeDrawable.draw(canvas, cell);
    }
}
