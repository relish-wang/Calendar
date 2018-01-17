package wang.relish.calendar;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * 日期文字
 *
 * @author Relish Wang
 * @since 2017/11/23
 */
/* package */ class DateDrawable extends IDrawable {

    /**
     * 根据设计稿上尺寸标注得：
     * 文字大小占屏幕宽度的32/750, 约等于0.04266666667
     */
    /* package */ static final float DATE_TEXT_SIZE_SCALE = 0.04266666667f;

    /**
     * 根据设计稿上尺寸标注得：
     * 今天文字大小占屏幕宽度的24/750, 等于0.032
     */
    /* package */ static final float TODAY_TEXT_SIZE_SCALE = 0.032f;

    /**
     * 文字颜色
     */
    private int textColor;
    /**
     * 日期
     * eg: 27
     */
    private String text;

    /**
     * 文字大小与屏宽的比例
     */
    private float textSizeScale;

    /* package */ DateDrawable(String text, int textColor, float textSizeScale) {
        this.textColor = textColor;
        this.text = text;
        this.textSizeScale = textSizeScale;
    }

    @Override
    public void draw(@NonNull Canvas canvas, RectF cell) {
        float textSize = cell.width() * 7 * DATE_TEXT_SIZE_SCALE;
        if (this.textSizeScale != 0) {
            textSize = cell.width() * 7 * this.textSizeScale;
        }
        mPaint.setTextSize(textSize);
        float textWidth = mPaint.measureText(text);
        float textHeight = mPaint.ascent() + mPaint.descent();
        mPaint.setColor(textColor);
        canvas.drawText(text, cell.centerX() - textWidth / 2, cell.centerY() - textHeight / 2, mPaint);
    }
}
