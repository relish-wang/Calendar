package wang.relish.calendar;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * @author Relish Wang
 * @since 2017/12/26
 */
class DefaultAdapter extends MonthAdapter<MonthStyle, DateStyle> {

    public DefaultAdapter() {
        this(Utils.getMonthStyleDemo());
    }

    public DefaultAdapter(MonthStyle monthStyle) {
        super(monthStyle);
    }

    @Override
    public DateStyle transform(DateStyle item) {
        return item;
    }

    @Override
    public void onDrawCell(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle) {
        // 1 选中样式
        IDrawable activeDrawable = dateStyle.getActiveDrawable();
        if (activeDrawable != null) activeDrawable.draw(canvas, cell);
        // 2 日期文字
        IDrawable dateDrawable = dateStyle.getDateDrawable();
        if (dateDrawable != null) dateDrawable.draw(canvas, cell);
    }
}
