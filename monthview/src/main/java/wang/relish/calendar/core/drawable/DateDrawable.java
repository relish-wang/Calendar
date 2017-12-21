package wang.relish.calendar.core.drawable;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * 日期文字
 *
 * @author Relish Wang
 * @since 2017/11/23
 */
public class DateDrawable extends IDrawable {


    /**
     * 根据设计稿上尺寸标注得：
     * 文字大小占屏幕宽度的32/750, 约等于0.04266666667
     */
    private static final float DATE_TEXT_SIZE_SCALE = 0.04266666667f;

    /**
     * 根据设计稿上尺寸标注得：
     * 今天文字大小占屏幕宽度的24/750, 等于0.032
     */
    private static final float TODAY_TEXT_SIZE_SCALE = 0.032f;

    /**
     * 文字颜色
     */
    private int textColor;
    /**
     * 日期
     * eg: 27
     */
    private String text;

    private boolean isToday = false;

    public DateDrawable(String text, int textColor, boolean isToday) {
        this.textColor = textColor;
        this.text = text;
        this.isToday = isToday;

    }

    private float textHeight;
    private float textWidth;

    @Override
    public void draw(@NonNull Canvas canvas, RectF cell) {
        float textSize = cell.width() * 7 * (isToday ? TODAY_TEXT_SIZE_SCALE : DATE_TEXT_SIZE_SCALE);
        mPaint.setTextSize(textSize);
        textWidth = mPaint.measureText(text);
        textHeight = mPaint.ascent() + mPaint.descent();
        mPaint.setColor(textColor);
        canvas.drawText(text, cell.centerX() - textWidth / 2, cell.centerY() - textHeight / 2, mPaint);
    }

    @Override
    public int intrinsicWidth() {
        return (int) textWidth;
    }

    @Override
    public int intrinsicHeight() {
        return (int) textHeight;
    }
}
