package wang.relish.calendar.pager.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import wang.relish.calendar.IDrawable;

/**
 * 小红点(不带数字)
 *
 * @author Relish Wang
 * @since 2017/11/22
 */
public class RedPointDrawable extends IDrawable {
    /**
     * 根据设计稿上尺寸标注得：
     * 小红点半径占屏幕宽度的8/750,约等于0.01066666667
     */
    private static final float RADIUS_IN_SCALE = 0.01066666667f;
    /**
     * 根据设计稿上尺寸标注得：
     * 小红点半径占屏幕宽度的10/750,约等于0.01333333333
     */
    private static final float RADIUS_OUT_SCALE = 0.01333333333f;

    /**
     * 根据设计稿上尺寸标注得：
     * 小红点圆心横向距cell中心为屏幕宽度的22/750,约等于0.02933333333
     */
    private static final float CENTER_X_OFFSET = 0.02933333333f;

    /**
     * 根据设计稿上尺寸标注得：
     * 小红点圆心纵向距cell中心为屏幕宽度的24/750,约等于0.032
     */
    private static final float CENTER_Y_OFFSET = 0.032f;
    /**
     * 根据设计稿上颜色标注得：
     * 小红点的内圈颜色为#FF0000
     */
    private static final int RED_POINT_COLOR = Color.parseColor("#FF4040");
    /**
     * 根据设计稿上颜色标注得：
     * 小红点的外圈颜色为#FF0000
     */
    private static final int RED_POINT_BG_COLOR = Color.parseColor("#FFFFFF");

    private float mInRadius;
    private float mOutRadius;

    public RedPointDrawable() {
        mPaint.setColor(RED_POINT_COLOR);
    }

    @Override
    public void draw(@NonNull Canvas canvas, RectF cell) {
        float screenWidth = cell.width() * 7;
        float centerX = cell.centerX() + screenWidth * CENTER_X_OFFSET;
        float centerY = cell.centerY() - screenWidth * CENTER_Y_OFFSET;
        // 1 外圈
        mOutRadius = screenWidth * RADIUS_OUT_SCALE;
        mPaint.setColor(RED_POINT_BG_COLOR);
        canvas.drawCircle(centerX, centerY, mOutRadius, mPaint);
        // 2 内圆
        mInRadius = screenWidth * RADIUS_IN_SCALE;
        mPaint.setColor(RED_POINT_COLOR);
        canvas.drawCircle(centerX, centerY, mInRadius, mPaint);
    }

//    @Override
//    public int intrinsicWidth() {
//        return (int) (mOutRadius * 2);
//    }
//
//    @Override
//    public int intrinsicHeight() {
//        return (int) (mOutRadius * 2);
//    }
}
