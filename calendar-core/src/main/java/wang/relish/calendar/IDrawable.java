package wang.relish.calendar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 基于日期格子而绘制的样式基类
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
     * @param canvas 画笔
     * @see #draw(Canvas, RectF)
     * 注： 不能直接使用此方法
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    @Override
    public void draw(@NonNull Canvas canvas) {
        // never use it
        throw new UnsupportedOperationException("please use IDrawable#draw(@NonNull Canvas canvas, RectF cell)");
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


    /**
     * Return the intrinsic width of the underlying drawable object.  Returns
     * -1 if it has no intrinsic width, such as with a solid color.
     */
    @Override
    public int getIntrinsicWidth() {
        return -1;
    }

    /**
     * Return the intrinsic height of the underlying drawable object. Returns
     * -1 if it has no intrinsic height, such as with a solid color.
     */
    @Override
    public int getIntrinsicHeight() {
        return -1;
    }
}
