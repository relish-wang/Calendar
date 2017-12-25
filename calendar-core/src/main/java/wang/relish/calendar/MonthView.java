package wang.relish.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.Arrays;

/**
 * 显示月份
 *
 * @author wangxin
 * @since 2017/3/15
 */

public class MonthView extends View {

    //------------------魔鬼数字相关_start------------------------
    /**
     * 设计稿上一个格子的高度是屏幕宽度的104/750
     */
    public static final float CELL_HEIGHT_SCALE = 0.1386666667f;
    /**
     * 设计稿上bottom_padding的高度是屏幕宽度的27/750
     */
    private static final float BOTTOM_PADDING_SCALE = 0.036f;
    //------------------魔鬼数字相关_end------------------------

    //------------------尺寸相关_start-------------------------
    /**
     * 一行有7天
     */
    private static final int NUM_COLUMNS = 7;
    /**
     * 一个月有多少行（可能4行或5行或6行）
     * 参考钉钉是固定6行的
     */
    private static final int NUM_ROWS = 6;

    /**
     * 格子宽
     */
    private float mCellWidth;
    /**
     * 格子高
     */
    protected float mCellHeight;
    //------------------尺寸相关_end---------------------------

    /**
     * 显示的数据
     */
    protected MonthStyle mMonthStyle;

    /**
     * 监听器
     */
    private OnSelectListener mListener;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mMonthStyle = Utils.getMonthStyleDemo();
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        float bottomPadding = widthSize * BOTTOM_PADDING_SCALE;
        mCellWidth = widthSize * 1.0f / NUM_COLUMNS;
        mCellHeight = widthSize * CELL_HEIGHT_SCALE;
        int heightSize = (int) (NUM_ROWS * mCellHeight);
        int measureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        setMeasuredDimension(measureSpec, heightSize);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        mCellWidth = getWidth() * 1.0F / NUM_COLUMNS;

        DateStyle[] dateStyle = mMonthStyle.getDateStyle();
        if (dateStyle == null || dateStyle.length != 42)
            throw new IllegalArgumentException("dateStyle is damaged!:" + Arrays.toString(dateStyle));

        int row;
        int col;
        RectF cell;

        for (int i = 0; i < dateStyle.length; i++) {
            row = i / 7;
            col = i % 7;
            float l = col * mCellWidth;
            float t = row * mCellHeight;
            cell = new RectF(l, t, l + mCellWidth, t + mCellHeight);
            DateStyle item = dateStyle[i];
            if (item == null) continue;
            // 绘制样式
            onDrawCell(canvas, cell, item);
        }
    }

    /**
     * 绘制日期的各种样式
     *
     * @param canvas    画布
     * @param cell      样式绘制的区域
     * @param dateStyle 日期样式们
     */
    public void onDrawCell(Canvas canvas, @NonNull RectF cell, @NonNull DateStyle dateStyle) {
        // 1 选中样式
        IDrawable activeDrawable = dateStyle.getActiveDrawable();
        if (activeDrawable != null) activeDrawable.draw(canvas, cell);
        // 2 日期文字
        IDrawable dateDrawable = dateStyle.getDateDrawable();
        if (dateDrawable != null) dateDrawable.draw(canvas, cell);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int currYear = mMonthStyle.getYear();
            int currMonth = mMonthStyle.getMonth();
            int currDay = mMonthStyle.getDay();
            int weekFirstDay = mMonthStyle.getWeekFirstDay();
            float x = event.getX();
            float y = event.getY();
            int cellX = (int) (x / mCellWidth);
            int cellY = (int) (y / mCellHeight);

            //异常坐标
            if (cellX < 0 || cellX > 7 || cellY < 0 || cellY > 6) return true;

            int preMonthTailDayCount = Utils.preMonthTailDayCount(currYear, currMonth, weekFirstDay);
            Point monthLastDayPoint = Utils.getMonthLastDayPoint(currYear, currMonth, weekFirstDay);

            if (cellY == 0 && cellX < preMonthTailDayCount) {
                int year = currMonth == 0 ? currYear - 1 : currYear;
                int month = currMonth == 0 ? 11 : currMonth - 1;
                int monthDayCount = Utils.getMonthDayCount(year, month);
                int day = monthDayCount - preMonthTailDayCount + (cellX + 1);
                // 点击到了前一个月的末尾的某个日期
                if (mListener != null) {
                    Logger.d(year + "/" + month + "/" + day);
                    mListener.onPrevMonthDateSelect(this, year, month, day);
                    invalidate();
                    return false;
                } else {
                    return true;
                }
            } else if (cellY == monthLastDayPoint.y && cellX >= monthLastDayPoint.x
                    || cellY > monthLastDayPoint.y) {
                int year = currMonth == 11 ? currYear + 1 : currYear;
                int month = currMonth == 11 ? 0 : currMonth + 1;
                int frontDayCount = preMonthTailDayCount + Utils.getMonthDayCount(currYear, currMonth);
                int day = cellY * 7 + (cellX + 1) - frontDayCount;
                // 点击到了后一个月的末尾几个日期
                if (mListener != null) {
                    mListener.onNextMonthDateSelect(this, year, month, day);
                    invalidate();
                    return false;
                } else {
                    return true;
                }
            } else {
                int day = cellY * 7 + (cellX + 1) - preMonthTailDayCount;
                if (day == currDay) return true;
                // 点击到了中间的日期
                selectedPositionChanged(day);
                if (mListener != null) {
                    mListener.onCurrMonthDateSelect(this, currYear, currMonth, day);
                    invalidate();
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    /**
     * 选中样式变更
     *
     * @param afterDay 现在选中的日期
     */
    private synchronized void selectedPositionChanged(int afterDay) {
        if (mMonthStyle.getDay() == afterDay) return;
        mMonthStyle.setSelectedDay(afterDay);
        invalidate();
    }


    /**
     * 设置日期选择回调的监听器
     *
     * @param listener 日期选择回调监听器
     */
    public void setOnSelectListener(OnSelectListener listener) {
        mListener = listener;
    }

    /**
     * 设置所有的Item属性
     *
     * @param monthStyle Item们的属性
     */
    public void setMonthStyle(MonthStyle monthStyle) {
        mMonthStyle = monthStyle;
        invalidate();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        throw new UnsupportedOperationException("CANNOT do that!");
    }
}
