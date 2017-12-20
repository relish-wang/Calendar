package wang.relish.datepicker.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * @author Relish Wang
 * @since 2017/12/19
 */
public class CustomView extends View {
    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#FF0000"));
        pointF = new PointF(0, 0);
    }

    PointF pointF;
    Paint paint;
    int radius = 20;

    float[] pathPos = new float[]{200f, 300f};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(pointF.x, pointF.y, radius, paint);

        Random random = new Random();
        PathMeasure pathMeasure = new PathMeasure();
        if (getGesturePath() != null) {
            final short steps = 150;
            final byte stepDistance = 5;
            final byte maxTrailRadius = 15;
            pathMeasure.setPath(getGesturePath(), false);
            final float pathLength = pathMeasure.getLength();
            for (short i = 1; i <= steps; i++) {
                final float distance = pathLength - i * stepDistance;
                if (distance >= 0) {
                    final float trailRadius = maxTrailRadius * (1 - (float) i / steps);
                    pathMeasure.getPosTan(distance, pathPos, null);
                    final float x = pathPos[0] + RandomUtils.nextFloat(0, 2 * trailRadius) - trailRadius;
                    final float y = pathPos[1] + RandomUtils.nextFloat(0, 2 * trailRadius) - trailRadius;
                    paint.setShader(new RadialGradient(
                            x,
                            y,
                            trailRadius > 0 ? trailRadius : Float.MIN_VALUE,
                            ColorUtils.setAlphaComponent(Color.GREEN, random.nextInt(0xff)),
                            Color.TRANSPARENT,
                            Shader.TileMode.CLAMP
                    ));
                    canvas.drawCircle(x, y, trailRadius, paint);
                }
            }
        }
    }

    private Path getGesturePath() {
        Path path = new Path();
        path.addCircle(500, 500, 200, Path.Direction.CCW);
        return path;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointF.set(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                pointF.set(event.getX(), event.getY());
                this.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                pointF.set(event.getX(), event.getY());
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

}
