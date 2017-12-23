package wang.relish.datepicker.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;

import wang.relish.datepicker.MonthStyle;


/**
 * 今天背景样式
 * Created by 王鑫 on 2017/3/17.
 */
public class TodayDrawable extends CustomDrawable {
    private Paint mPaint;
    private int mRadius;


    public TodayDrawable() {
        mPaint = new Paint();
        mPaint.setColor(MonthStyle.TODAY_BG_COLOR);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setCellSize(int cellSize) {
        mCellSize = cellSize;
        mRadius = cellSize / 2 * 80 / 107;// / 2 / 2;//直径为正方形边长的一半；半径为直径的一半
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
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