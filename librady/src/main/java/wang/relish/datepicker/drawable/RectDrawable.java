package wang.relish.datepicker.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;

import wang.relish.datepicker.MonthStyle;


/**
 * MonthView的选中日期样式（区间）
 * Created by 王鑫 on 2017/3/17.
 */
public class RectDrawable extends CustomDrawable {
    private Paint mPaint;
    private int mCellSize;
    private int mRadius;


    public RectDrawable() {
        mPaint = new Paint();
        mPaint.setColor(MonthStyle.WITHIN_RANGE_COLOR);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setCellSize(int cellSize) {
        mCellSize = cellSize;
        mRadius = mCellSize / 2 * 80 / 107;//mCellSize / 2 / 2;//直径为正方形边长的一半；半径为直径的一半
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRect(startX, startY + mCellSize / 2 - mRadius, startX + mCellSize, startY + mCellSize / 2 + mRadius, mPaint);
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