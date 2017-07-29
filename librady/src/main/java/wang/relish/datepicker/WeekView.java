package wang.relish.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 仅显示星期行的View
 * Created by 王鑫 on 2017/3/23.
 */
public class WeekView extends View {


    private int mBgColor = Color.parseColor("#FFFFFF");

    /**
     * 一周7天
     */
    private static final int NUM_COLUMNS = 7;

    private float mCellSize;

    private float mWeekCellHeight;

    private int mWeekTextSize = 48;

    private int mWeekTextColor;

    private Paint mPaint;

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.parseColor("#ffffff"));

        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        mCellSize = widthSize / NUM_COLUMNS;
        int heightSize = (int) mCellSize;
        int measureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        setMeasuredDimension(measureSpec, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCellSize == 0) {
            mCellSize = getWidth() / NUM_COLUMNS;
        }
        //周日~周六的字符串数组
        String[] weekString = getContext().getResources().getStringArray(R.array.weekday);
        for (int i = 0; i < weekString.length; i++) {
            //绘制白色背景矩形
            float startRecX = mCellSize * i;
            float startRecY = 0;
            float endRecX = startRecX + mWeekCellHeight;
            float endRecY = startRecY + mWeekCellHeight;
            mPaint.setColor(mBgColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);

            mPaint.setTextSize(mWeekTextSize);
            float startX = startRecX + (mCellSize - mPaint.measureText(weekString[i])) / 2;
            float startY = startRecY + mCellSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2;
            mPaint.setColor(mWeekTextColor == 0 ? MonthStyle.WEEK_TEXT_COLOR : mWeekTextColor);
            canvas.drawText(weekString[i], startX, startY, mPaint);
        }
    }

    public void setBackgroundColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }

    public void setWeekCellHeight(float mWeekCellHeight) {
        this.mWeekCellHeight = mWeekCellHeight;
    }

    public void setWeekTextSize(int mWeekTextSize) {
        this.mWeekTextSize = mWeekTextSize;
    }

    public void setWeekTextColor(int mWeekTextColor) {
        this.mWeekTextColor = mWeekTextColor;
    }
}
