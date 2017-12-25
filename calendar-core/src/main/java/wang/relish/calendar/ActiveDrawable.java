package wang.relish.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * 选中的样式
 *
 * @author wangxin
 * @since 2017/11/23
 */
/* package */ class ActiveDrawable extends IDrawable {
    /**
     * 设计稿上的颜色
     * 被选中日期的圆形背景
     */
    private static final int ACTIVE_DRAWABLE_COLOR = Color.parseColor("#FFEEE8");

    /**
     * 设计稿上的该图形的半径为屏幕宽度的32/750
     */
    public static final float ACTIVE_DRAWABLE_RADIUS_SCALE = 0.04266666667f;

    private float mRadius;

    /**
     * 使用此方法进行绘制
     *
     * @param canvas 画笔
     * @param cell   格子
     */
    public void draw(@NonNull Canvas canvas, RectF cell) {
        mPaint.setColor(ACTIVE_DRAWABLE_COLOR);
        mRadius = cell.width() * 7 * ACTIVE_DRAWABLE_RADIUS_SCALE;
        canvas.drawCircle(cell.centerX(), cell.centerY(), mRadius, mPaint);
    }

//    @Override
//    public int intrinsicWidth() {
//        return (int) (mRadius * 2);
//    }
//
//    @Override
//    public int intrinsicHeight() {
//        return (int) (mRadius * 2);
//    }
}