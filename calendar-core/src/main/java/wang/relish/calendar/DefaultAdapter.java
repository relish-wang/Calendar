package wang.relish.calendar;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * 默认的日历适配器
 * (只显示日期样式)
 *
 * @author Relish Wang
 * @since 2017/12/26
 */
public class DefaultAdapter extends MonthAdapter {

    /* package */ DefaultAdapter() {
        this(Utils.getMonthStyleDemo());
    }

    public DefaultAdapter(MonthStyle monthStyle) {
        super(monthStyle);
    }

    @Override
    protected void drawUnderDate(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle) {
        // do nothing.
    }

    @Override
    protected void drawAboveDate(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle) {
        // do nothing.
    }
}
