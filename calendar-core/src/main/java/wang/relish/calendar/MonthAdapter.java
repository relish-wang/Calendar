package wang.relish.calendar;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 * 一个月的日历样式的适配器
 *
 * @author Relish Wang
 * @since 2017/12/26
 */
public abstract class MonthAdapter {

    private MonthStyle mMonthStyle;

    public MonthAdapter(@NonNull MonthStyle monthStyle) {
        mMonthStyle = monthStyle;
    }

    /**
     * 绘制在日期下方的样式
     * 先绘制的在下方
     *
     * @param canvas    画布
     * @param cell      日期格子
     * @param dateStyle 日期样式
     */
    protected abstract void drawUnderDate(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle);

    /**
     * 绘制在日期上方的样式
     * 后绘制的在上方
     *
     * @param canvas    画布
     * @param cell      日期格子
     * @param dateStyle 日期样式
     */
    protected abstract void drawAboveDate(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle);

    /**
     * draw calendar
     *
     * @param canvas canvas
     * @param width  width of each cell
     * @param height height of each cell
     */
    /* package */ void draw(Canvas canvas, float width, float height) {
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
            onDrawCell(canvas, cell, item);
        }
    }


    /**
     * 绘制具体格子数据
     *
     * @param canvas    画布
     * @param cell      当前格子
     * @param dateStyle 日期格子内所有样式
     */
    private void onDrawCell(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle) {
        drawUnderDate(canvas, cell, dateStyle);

        IDrawable dateDrawable = dateStyle.getDateDrawable();
        if (dateDrawable != null) dateDrawable.draw(canvas, cell);

        drawAboveDate(canvas, cell, dateStyle);
    }

    protected int getRow() {
        return Utils.getMonthRowCount(mMonthStyle.getYear(), mMonthStyle.getMonth(), mMonthStyle.getWeekFirstDay());
    }

    public MonthStyle getMonthStyle() {
        return mMonthStyle;
    }

    private final DataSetObservable mObservable = new DataSetObservable();
    private DataSetObserver mMonthObserver;

    void setMonthObserver(DataSetObserver observer) {
        synchronized (this) {
            mMonthObserver = observer;
        }
    }

    /**
     * This method should be called by the application if the data backing this adapter has changed
     * and associated views should update.
     */
    public void notifyDataSetChanged() {
        synchronized (this) {
            if (mMonthObserver != null) {
                mMonthObserver.onChanged();
            }
        }
        mObservable.notifyChanged();
    }

    /**
     * Register an observer to receive callbacks related to the adapter's data changing.
     *
     * @param observer The {@link android.database.DataSetObserver} which will receive callbacks.
     */
    public void registerDataSetObserver(@NonNull DataSetObserver observer) {
        setMonthObserver(observer);
        mObservable.registerObserver(observer);
    }

    /**
     * Unregister an observer from callbacks related to the adapter's data changing.
     *
     * @param observer The {@link android.database.DataSetObserver} which will be unregistered.
     */
    public void unregisterDataSetObserver(@NonNull DataSetObserver observer) {
        mObservable.unregisterObserver(observer);
    }

}
