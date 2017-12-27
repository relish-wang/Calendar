package wang.relish.calendar;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 * @author Relish Wang
 * @since 2017/12/26
 */
public abstract class MonthAdapter<M extends MonthStyle, D extends DateStyle> {

    private M mMonthStyle;

    public MonthAdapter(M monthStyle) {
        mMonthStyle = monthStyle;
    }


    void draw(Canvas canvas, float width, float height) {
        DateStyle[] dateStyle = mMonthStyle.getDateStyle();
        if (dateStyle == null || dateStyle.length != 42)
            throw new IllegalArgumentException("dateStyle is damaged!:" + Arrays.toString(dateStyle));

        int row;
        int col;
        RectF cell;

        for (int i = 0; i < dateStyle.length; i++) {
            row = i / 7;
            col = i % 7;
            float l = col * width;
            float t = row * height;
            cell = new RectF(l, t, l + width, t + height);
            DateStyle item = dateStyle[i];
            if (item == null) continue;
            // 绘制样式
            //noinspection unchecked

            onDrawCell(canvas, cell, transform(item));
        }
    }

    public abstract D transform(DateStyle item);

    /**
     * 绘制具体格子数据
     *
     * @param canvas    画布
     * @param cell      当前格子
     * @param dateStyle
     */
    public abstract void onDrawCell(Canvas canvas, @NonNull RectF cell, @NonNull D dateStyle);

    public M getMonthStyle() {
        return mMonthStyle;
    }
}
