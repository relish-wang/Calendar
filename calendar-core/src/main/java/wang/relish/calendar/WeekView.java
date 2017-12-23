package wang.relish.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.Arrays;
import java.util.Calendar;

import wang.relish.calendar.core.R;

/**
 * 星期行
 *
 * @author Relish Wang
 * @since 2017/11/20
 */
public final class WeekView extends View {

    /**
     * 根据设计稿上颜色标注得：
     * 星期文字的字体颜色为:#7e8283
     */
    private static final int WEEK_TEXT_COLOR = Color.parseColor("#7e8283");

    /**
     * 根据设计稿上尺寸标注得：
     * 星期行的高度占屏幕宽度的64/750,约等于0.08533333333
     */
    private static final float WEEK_HEIGHT_SCALE = 0.08533333333f;
    /**
     * 根据设计稿上尺寸标注得：
     * 星期文字的大小占屏幕宽度的28/750,约等于0.03733333333
     */
    private static final float TEXT_SIZE_HEIGHT_SCALE = 0.03733333333f;

    /**
     * 一周7天
     */
    private static final int NUM_COLUMNS = 7;

    /**
     * 格子宽度
     * 屏幕宽度/7
     */
    private float mWeekCellWidth;

    /**
     * 格子高度
     * 默认50dp
     */
    private float mWeekCellHeight;

    /**
     * 字体大小
     * 默认15sp
     */
    private float mWeekTextSize = 15;

    /**
     * 星期文字的颜色
     */
    private int mWeekTextColor = Color.parseColor("#666666");

    /**
     * 周的第一天（默认为周日）
     * 1~7: 周日~周六
     */
    private int mWeekFirstDay = Calendar.MONDAY;

    private Paint mPaint = new Paint();

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        mWeekCellWidth = 1.0f * widthSize / NUM_COLUMNS;
        mWeekCellHeight = widthSize * WEEK_HEIGHT_SCALE;
        mWeekTextSize = widthSize * TEXT_SIZE_HEIGHT_SCALE;
        int measureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        setMeasuredDimension(measureSpec, (int) mWeekCellHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWeekCellWidth == 0) {
            int screenWidth = getWidth();
            mWeekCellWidth = 1.0f * screenWidth / NUM_COLUMNS;
            mWeekCellHeight = 1.0f * screenWidth * TEXT_SIZE_HEIGHT_SCALE;
        }
        //周日~周六的字符串数组
        String[] weekString = getContext().getResources().getStringArray(R.array.weekday);

        int length = weekString.length;

        String[] left = Arrays.copyOfRange(weekString, 0, mWeekFirstDay - 1);
        String[] right = Arrays.copyOfRange(weekString, mWeekFirstDay - 1, length);

        for (int i = 0; i < length; i++) {
            if (i < right.length) {
                weekString[i] = right[i];
            } else {
                weekString[i] = left[i - right.length];
            }
        }

        for (int i = 0; i < weekString.length; i++) {
            float startRecX = mWeekCellWidth * i;
            float startRecY = 0;
            mPaint.setTextSize(mWeekTextSize);//单位px
            float startX = startRecX + (mWeekCellWidth - mPaint.measureText(weekString[i])) / 2;
            float startY = startRecY + mWeekCellHeight / 2 - (mPaint.ascent() + mPaint.descent()) / 2;
            mPaint.setColor(mWeekTextColor == 0 ? WEEK_TEXT_COLOR : mWeekTextColor);
            canvas.drawText(weekString[i], startX, startY, mPaint);
        }
    }

    /**
     * 设置文字大小（单位：px）
     * 推荐使用#setWeekTextSizeSp(int)方法
     *
     * @param weekTextSize 字体大小(px)
     * @see #setWeekTextSizeSp(int)
     */
    @Deprecated
    public void setWeekTextSize(int weekTextSize) {
        this.mWeekTextSize = weekTextSize;
        invalidate();
    }

    /**
     * 设置文字大小（单位：sp）
     *
     * @param weekTextSize 字体大小(sp)
     */
    public void setWeekTextSizeSp(int weekTextSize) {
        this.mWeekTextSize = applyDimension(TypedValue.COMPLEX_UNIT_SP, weekTextSize);
        invalidate();
    }

    public void setWeekTextColor(int weekTextColor) {
        this.mWeekTextColor = weekTextColor;
        invalidate();
    }

    /**
     * @param dayOfWeek one of seven below
     * @see Calendar#SUNDAY
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     */
    public void setWeekFirstDay(int dayOfWeek) {
        this.mWeekFirstDay = dayOfWeek;
        invalidate();
    }

    /**
     * Converts an unpacked complex data value holding a dimension to its final floating
     * point value. The two parameters <var>unit</var> and <var>value</var>
     * are as in {@link TypedValue#TYPE_DIMENSION}.
     *
     * @param unit  The unit to convert from.
     * @param value The value to apply the unit to.
     * @return The complex floating point value multiplied by the appropriate
     * metrics depending on its unit.
     */
    protected int applyDimension(int unit, float value) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float result = value;
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                result = value;
                break;
            case TypedValue.COMPLEX_UNIT_DIP:
                result = value * metrics.density;
                break;
            case TypedValue.COMPLEX_UNIT_SP:
                result = value * metrics.scaledDensity;
                break;
            case TypedValue.COMPLEX_UNIT_PT:
                result = value * metrics.xdpi * (1.0f / 72);
                break;
            case TypedValue.COMPLEX_UNIT_IN:
                result = value * metrics.xdpi;
                break;
            case TypedValue.COMPLEX_UNIT_MM:
                result = value * metrics.xdpi * (1.0f / 25.4f);
                break;
        }
        return (int) ((result >= 0) ? (result + 0.5f) : (result - 0.5f));
    }
}