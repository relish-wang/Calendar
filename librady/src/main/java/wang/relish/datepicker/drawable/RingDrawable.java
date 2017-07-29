package wang.relish.datepicker.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import wang.relish.datepicker.MonthStyle;


/**
 * 选择同一个日期两次（圆环样式）
 * <p>
 * Created by 王鑫 on 2017/3/17.
 */
public class RingDrawable extends CustomDrawable {
    private Paint mPaint;
    private int mRadius;
    private int outRadius;


    public RingDrawable() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setCellSize(int cellSize) {
        mCellSize = cellSize;
        mRadius = mCellSize / 2 * 80 / 107;//cellSize / 2 / 2;//直径为正方形边长的一半；半径为直径的一半
        outRadius = mCellSize / 2 * 90 / 107;//外圆半径
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mPaint.setColor(MonthStyle.ENDS_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);//只绘制轮廓
        mPaint.setStrokeWidth(2);//2像素
        RectF rect = new RectF(startX + mCellSize / 2 - outRadius, startY + mCellSize / 2 - outRadius,
                startX + mCellSize / 2 + outRadius, startY + mCellSize / 2 + outRadius);
        canvas.drawArc(rect, 0, 360, false, mPaint);

        mPaint.setStyle(Paint.Style.FILL);//只绘制轮廓
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