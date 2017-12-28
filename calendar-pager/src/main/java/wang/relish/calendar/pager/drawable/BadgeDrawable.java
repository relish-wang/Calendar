package wang.relish.calendar.pager.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import wang.relish.calendar.IDrawable;

/**
 * 带数字的徽标
 *
 * @author Relish Wang
 * @since 2017/11/23
 */
public class BadgeDrawable extends IDrawable {

    /**
     * 根据设计稿上尺寸标注得：
     * 徽标半径占屏幕宽度的18/750,等于0.024
     */
    private static final float BADGE_IN_RADIUS_SCALE = 0.024f;
    /**
     * 根据设计稿上尺寸标注得：
     * 徽标半径占屏幕宽度的20/750,等于0.02666666667f;
     */
    private static final float BADGE_OUT_RADIUS_SCALE = 0.02666666667f;

    /**
     * 根据设计稿上尺寸标注得：
     * 徽标数字占屏幕宽度的24/750,等于0.032
     */
    private static final float TEXT_SIZE_SCALE = 0.032f;

    /**
     * 根据设计稿上尺寸标注得：
     * 徽标中心横向距cell中心为屏幕宽度的32/750,约
     */
    private static final float CENTER_X_OFFSET = 0.04266666667f;
    /**
     * 根据设计稿上尺寸标注得：
     * 徽标中心纵向距cell中心为屏幕宽度的32/750,等于0.04266666667
     */
    private static final float CENTER_Y_OFFSET = 0.04266666667f;
    /**
     * 根据设计稿上颜色标注得：
     * 徽标文字的颜色为#FFFFFF
     */
    private static final int TEXT_COLOR = Color.parseColor("#FFFFFF");
    /**
     * 根据设计稿上颜色标注得：
     * 徽标的主颜色为#FF0000
     */
    private static final int BADGE_IN_COLOR = Color.parseColor("#FF4040");
    /**
     * 根据设计稿上颜色标注得：
     * 徽标的外颜色为#FFFFFF
     */
    private static final int BADGE_OUTER_COLOR = Color.parseColor("#FFFFFF");

    private float outRadius;
    private float inRadius;
    private float textWidth;

    private float badgeViewWidth;
    private float badgeViewHeight;
    private float textSize;


    private String mBadgeNumber;

    public BadgeDrawable(String badgeNumber) {
        this.mBadgeNumber = badgeNumber;
    }

    private RectF getRect(RectF cell) {
        float screenWidth = cell.width() * 7;
        textSize = screenWidth * TEXT_SIZE_SCALE;
        mPaint.setTextSize(textSize);
        float oneLetterTextWidth = mPaint.measureText("6");
        textWidth = mPaint.measureText(mBadgeNumber);
        outRadius = screenWidth * BADGE_OUT_RADIUS_SCALE;
        inRadius = screenWidth * BADGE_IN_RADIUS_SCALE;
        float centerX = cell.centerX() + screenWidth * CENTER_X_OFFSET;
        float centerY = cell.centerY() - screenWidth * CENTER_Y_OFFSET;
        badgeViewWidth = outRadius * 2 + textWidth - oneLetterTextWidth;//
        badgeViewHeight = outRadius * 2;
        return new RectF(
                centerX - badgeViewWidth / 2,
                centerY - badgeViewHeight / 2,
                centerX + badgeViewWidth / 2,
                centerY + badgeViewHeight / 2);
    }


    @Override
    public void draw(@NonNull Canvas canvas, RectF cell) {
        RectF badgeView = getRect(cell);
        // 1 画白背景
        mPaint.setColor(BADGE_OUTER_COLOR);
        canvas.drawRoundRect(badgeView, outRadius, outRadius, mPaint);
        // 2 画红背景
        float dRadius = outRadius - inRadius;//内外半径的差值
        RectF inBadgeView = new RectF(
                badgeView.left + dRadius,
                badgeView.top + dRadius,
                badgeView.right - dRadius,
                badgeView.bottom - dRadius);
        mPaint.setColor(BADGE_IN_COLOR);
        canvas.drawRoundRect(inBadgeView, inRadius, inRadius, mPaint);
        // 3 画文字
        mPaint.setTextSize(textSize);
        mPaint.setColor(TEXT_COLOR);
        float textHeight = mPaint.ascent() + mPaint.descent();
        float startX = badgeView.centerX() - textWidth / 2;
        float startY = badgeView.centerY() - textHeight / 2;
        canvas.drawText(mBadgeNumber, startX, startY, mPaint);
    }

//    @Override
//    public int intrinsicWidth() {
//        return (int) badgeViewWidth;
//    }
//
//    @Override
//    public int intrinsicHeight() {
//        return (int) badgeViewHeight;
//    }
}
