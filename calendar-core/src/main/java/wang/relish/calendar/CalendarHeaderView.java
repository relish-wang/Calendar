package wang.relish.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import wang.relish.calendar.listener.OnChangeDateListener;
import wang.relish.calendar.listener.OnClickBackTodayListener;


/**
 * @author Relish Wang
 * @since 2017/12/18
 */
public class CalendarHeaderView extends View implements OnChangeDateListener {

    /**
     * 根据设计稿上尺寸标注得：
     * 星期行的高度占屏幕宽度的101/750,约等于0.0.1346666667
     */
    private static final float VIEW_HEIGHT_SCALE = 0.1346666667f;
    /**
     * 根据设计稿上尺寸标注得：
     * 年月文字高度占屏幕宽度的36/750,等于0.048
     */
    private static final float YEAR_TEXT_SIZE_SCALE = 0.048f;
    /**
     * 根据设计稿上尺寸标注得：
     * 回到今天文字高度占屏幕宽度的28/750,等于0.03733333333
     */
    private static final float TODAY_TEXT_SIZE_SCALE = 0.03733333333f;

    /**
     * 根据设计稿上尺寸标注得：
     * 年月文字x偏移距离占屏幕宽度的34/750,等于0.04533333333
     */
    private static final float YEAR_TEXT_X_OFFSET = 0.04533333333f;

    /**
     * 根据设计稿上尺寸标注得：
     * 年月文字y偏移距离占屏幕宽度的40/750,等于0.05333333333
     */
    private static final float YEAR_TEXT_Y_OFFSET = 0.05333333333f;

    /**
     * 根据设计稿上尺寸标注得：
     * 回到今天文字x偏移距离占屏幕宽度的34/750,等于0.04533333333
     */
    private static final float TODAY_TEXT_Y_OFFSET = 0.04533333333f;

    /**
     * 根据设计稿上尺寸标注得：
     * 回到今天文字y偏移距离占屏幕宽度的575/750,等于0.7666666667
     */
    private static final float TODAY_TEXT_X_OFFSET = 0.7666666667f;

    /**
     * 根据设计稿上尺寸标注得：
     * 回到今天文字所在矩形的高偏移距离占屏幕宽度的168/750,等于0.224
     */
    private static final float TODAY_RECT_WIDTH = 0.224f;

    /**
     * 根据设计稿上尺寸标注得：
     * 回到今天文字所在矩形的高偏移距离占屏幕宽度的56/750,等于0.07466666667
     */
    private static final float TODAY_RECT_HEIGHT = 0.07466666667f;

    private static final int YEAR_TEXT_COLOR = Color.parseColor("#1A1A1A");

    private static final int TODAY_TEXT_COLOR = Color.parseColor("#FF571A");

    private String backToday = "回到今天";

    public CalendarHeaderView(Context context) {
        super(context);
        init();
    }

    public CalendarHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Paint mPaint;

    private void init() {
        mPaint = new Paint();
    }

    private int year, month, day;

    private RectF mTodayRect;

    private float mWidth;
    private float mHeight;
    private float mYearTextSize;
    private float mTodayTextSize;
    private boolean isToday = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = mWidth * VIEW_HEIGHT_SCALE;
        mYearTextSize = mWidth * YEAR_TEXT_SIZE_SCALE;
        mTodayTextSize = mWidth * TODAY_TEXT_SIZE_SCALE;
        int measureSpec = MeasureSpec.makeMeasureSpec((int) mWidth, widthMode);
        setMeasuredDimension(measureSpec, (int) mHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mPaint.setColor(YEAR_TEXT_COLOR);
        float yearTextSize = mWidth * YEAR_TEXT_SIZE_SCALE;
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextSize(yearTextSize);
        float x = mWidth * YEAR_TEXT_X_OFFSET;
        float y = mHeight / 2 + yearTextSize / 2;
        canvas.drawText(year + "年" + (month + 1) + "月", x, y, mPaint);
        if (!isToday) {
            float w = mWidth * TODAY_RECT_WIDTH, h = mWidth * TODAY_RECT_HEIGHT;
            float l = mWidth * TODAY_TEXT_X_OFFSET, t = mHeight / 2 - h / 2;
            mTodayRect = new RectF(l, t, l + w, t + h);

            mPaint.setColor(TODAY_TEXT_COLOR);
            float textSize = mWidth * TODAY_TEXT_SIZE_SCALE;
            mPaint.setTypeface(Typeface.DEFAULT);
            mPaint.setTextSize(textSize);
            float textWidth = mPaint.measureText(backToday);
            float xOffset = mTodayRect.width() / 2 - textWidth / 2;
            float yOffset = mTodayRect.height() / 2 + textSize / 2;

            canvas.drawText("回到今天", mTodayRect.left + xOffset, mTodayRect.top + yOffset, mPaint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            if (mTodayRect == null) {
                invalidate();
                return true;
            }
            if (mTodayRect.contains(x, y)) {
                if (mListener != null) mListener.onClickBackToday();
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onChangeDate(Context context, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        isToday = Utils.isToday(year, month, day);
        invalidate();
    }

    private OnClickBackTodayListener mListener;

    public void setOnClickBackTodayListener(OnClickBackTodayListener listener) {
        mListener = listener;
    }
}
