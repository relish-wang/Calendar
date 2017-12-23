package wang.relish.datepicker.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;

import wang.relish.datepicker.MonthStyle;

/**
 * MonthView的结束日期样式
 * Created by 王鑫 on 2017/3/17.
 */
public class EndDrawable extends CustomDrawable {
    private Paint mPaint;
    private int mCellSize;
    /**
     * 圆圈半径
     */
    private int mRadius;


    public EndDrawable() {
        mPaint = new Paint();
        mPaint.setColor(MonthStyle.ENDS_COLOR);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setCellSize(int cellSize) {
        mCellSize = cellSize;

        mRadius = mCellSize / 2 * 80 / 107;// mCellSize / 2 / 2;//直径为正方形边长的一半；半径为直径的一半
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        //画左边的长方形
        mPaint.setColor(MonthStyle.WITHIN_RANGE_COLOR);
        canvas.drawRect(startX, startY + mCellSize / 2 - mRadius, startX + mCellSize / 2, startY + mCellSize / 2 + mRadius, mPaint);
        //画中间的原
        mPaint.setColor(MonthStyle.ENDS_COLOR);
        canvas.drawCircle(startX + mCellSize / 2, startY + mCellSize / 2, mRadius, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    // getIntrinsicWidth、getIntrinsicHeight主要是为了在View使用wrap_content的时候，提供一下尺寸
    @Override
    public int getIntrinsicHeight() {
        return mCellSize;
    }

    @Override
    public int getIntrinsicWidth() {
        return mCellSize;
    }

}