package wang.relish.calendar.core.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 样式基类
 *
 * @author Relish Wang
 * @since 2017/11/23
 */
public abstract class IDrawable extends Drawable {

    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * @param canvas 画笔
     * @param cell   日期格子
     */
    public abstract void draw(@NonNull Canvas canvas, RectF cell);

    /**
     * 宽度
     *
     * @return -1
     */
    public abstract int intrinsicWidth();

    /**
     * 高度
     *
     * @return -1
     */
    public abstract int intrinsicHeight();

    /**
     * @param canvas 画笔
     * @see #draw(Canvas, RectF)
     * 注： 不能直接使用此方法
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    @Override
    public void draw(@NonNull Canvas canvas) {
        // never use it
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return intrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return intrinsicHeight();
    }
}
