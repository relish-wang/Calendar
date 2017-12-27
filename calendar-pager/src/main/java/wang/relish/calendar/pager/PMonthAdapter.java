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
public class PMonthAdapter extends MonthAdapter<MonthStyle, PDateStyle> {

    public PMonthAdapter(MonthStyle monthStyle) {
        super(monthStyle);
    }

    @Override
    public PDateStyle transform(DateStyle item) {
        return PDateStyle.toPDateStyle(item);
    }

    @Override
    public void onDrawCell(Canvas canvas, @NonNull RectF cell, @NonNull PDateStyle pStyle) {
        // 1 选中样式
        IDrawable activeDrawable = pStyle.getActiveDrawable();
        if (activeDrawable != null) activeDrawable.draw(canvas, cell);
        // 2 日期文字
        IDrawable dateDrawable = pStyle.getDateDrawable();
        if (dateDrawable != null) dateDrawable.draw(canvas, cell);

        // TODO 这样不好, 使用者不知道要用👆那两个东西

        // 3 徽标
        IDrawable badgeDrawable = pStyle.getBadgeDrawable();
        if (badgeDrawable != null) badgeDrawable.draw(canvas, cell);
    }
}
