package wang.relish.calendar.pager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import wang.relish.calendar.pager.listener.OnChangeStatusListener;

/**
 * 外层布局
 * 用于拦截触摸事件，控制日期展开和收拢
 *
 * @author wangxin
 * @since 2017/11/28
 */
public class CalendarLayout extends FrameLayout {

    private static final String TAG = "CalendarLayout";

    private ViewGroup mContent;
    private ITopView mTopView;//MonthPager

    @Stage
    private int stage = Stage.FOLD;

    //是否处于滑动中
    private boolean isSliding = false;

    /**
     * mTopView.getMeasuredHeight()
     */
    private int topHeight;
    /**
     * 格子高度
     */
    private int mItemHeight;
    private int bottomViewTopHeight;
    // TopView站靠高度-收拢高度
    private int maxDistance;

    private OverScroller mScroller;
    /**
     * 滑动速度上限
     */
    private float mMaxVelocity;
    /**
     * 滑动速度下限
     */
    private float mMinVelocity;
    private int activePointerId;

    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return (float) (Math.pow(t, 5) + 1.0f);
        }
    };

    public CalendarLayout(Context context) {
        super(context);
        init();
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTopView = (ITopView) getChildAt(0);

        mContent = (ViewGroup) getChildAt(1);

        mTopView.setOnTopViewChangedListener(new ITopView.OnTopViewChangedListener() {

            @Override
            public void onLayoutChanged(ITopView topView) {
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mItemHeight = mTopView.getItemHeight();
        topHeight = mTopView.getMeasuredHeight();
        maxDistance = topHeight - mItemHeight;
        switch (stage) {
            case Stage.FOLD:
                bottomViewTopHeight = mItemHeight;
                break;
            case Stage.OPEN:
                bottomViewTopHeight = topHeight;
                break;
        }
        mContent.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - mTopView.getItemHeight(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mContent.offsetTopAndBottom(bottomViewTopHeight);
        if (stage == Stage.FOLD) {
            mTopView.offsetTopAndBottom(-mTopView.getItemTop());
        }
    }

    private void init() {
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        mMaxVelocity = vc.getScaledMaximumFlingVelocity();
        mMinVelocity = vc.getScaledMinimumFlingVelocity();
        mScroller = new OverScroller(getContext(), sInterpolator);
    }

    float ox, oy;
    boolean isClickContentView = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isFlag = false;// 是否将触摸事件传递给OnTouchEvent(true: 传递)

        //上下运动进行拦截
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ox = ev.getX();
                oy = ev.getY();
                isClickContentView = isClickView(mContent, ev);
                cancel();
                activePointerId = ev.getPointerId(0);

                int top = mContent.getTop();

                if (top < topHeight) {
                    stage = Stage.FOLD;
                } else {
                    stage = Stage.OPEN;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float y = ev.getY();
                float x = ev.getX();

                float xdiff = x - ox;// 与上次触摸点的x方向偏移量（可正负）
                float ydiff = y - oy;// 与上次触摸点的y方向偏移量（可正负）

                // y方向距离大于5像素 且 y方向偏移量绝对值大于x方向偏移量绝对值
                if (Math.abs(ydiff) > 5 && Math.abs(ydiff) > Math.abs(xdiff)) {
                    isFlag = true;

                    if (isClickContentView) {
                        boolean isScroll = isScroll(mContent);
                        if (ydiff > 0) {
                            //向下
                            if (stage == Stage.OPEN) {
                                return super.onInterceptTouchEvent(ev);
                            } else {
                                if (isScroll) {
                                    return super.onInterceptTouchEvent(ev);
                                }
                            }
                        } else {
                            //向上
                            if (stage == Stage.FOLD) {
                                return super.onInterceptTouchEvent(ev);
                            } else {
                                if (isScroll) {
                                    return super.onInterceptTouchEvent(ev);
                                }
                            }
                        }
                    }
                }
                ox = x;
                oy = y;
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return isSliding || isFlag || super.onInterceptTouchEvent(ev);
    }

    /**
     * whether {@link #mContent} can scroll or not
     *
     * @param listView mContent
     * @return whether can scroll or not
     */
    private boolean isScroll(ViewGroup listView) {
        boolean canScroll = canScroll(listView);
        return stage == Stage.FOLD && canScroll;
    }

    private boolean canScroll(ViewGroup viewGroup) {
        if (!isPointInView(viewGroup, ox, oy)) return false;
        if (viewGroup.canScrollVertically(-1)) return true;
        int childCount = viewGroup.getChildCount();
        if (childCount == 0) return false;
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (!isPointInView(child, ox, oy)) continue;
            boolean canScrollUp = child.canScrollVertically(-1);//向上
            if (canScrollUp) return true;
            if (!(child instanceof ViewGroup)) continue;
            ViewGroup gChild = ((ViewGroup) child);
            if (gChild.getChildCount() == 0) continue;
            boolean canChildScrollUp = canScroll((ViewGroup) child);
            if (canChildScrollUp) return true;
        }
        return false;
    }

    private static boolean isPointInView(View v, float x, float y) {
        Rect rect = new Rect();
        v.getHitRect(rect);
        return rect.contains((int) x, (int) y);
    }


    public boolean isClickView(View view, MotionEvent ev) {
        Rect rect = new Rect();
        view.getHitRect(rect);
        boolean isClick = rect.contains((int) ev.getX(), (int) ev.getY());
//        Log.d(TAG, "isClickView() called with: isClick = [" + isClick + "]");
        return isClick;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        processTouchEvent(event);
        return true;
    }

    // 速度跟踪器, 专门用来跟踪触摸手势速度
    private VelocityTracker mVelocityTracker;


    public void processTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScroll(mContent)) return;
                if (isSliding) {
                    return;
                }
                float cy = event.getY();
                int dy = (int) (cy - oy);

                if (dy == 0) {
                    return;
                }
                oy = cy;
                move(dy);
                break;
            case MotionEvent.ACTION_UP:

                if (isSliding) {
                    cancel();
                    return;
                }

                //判断速度
                final int pointerId = activePointerId;
                // 计算速度: xxx px/s
                mVelocityTracker.computeCurrentVelocity(1, mMaxVelocity);
                float currentV = mVelocityTracker.getYVelocity(pointerId);

                if (Math.abs(currentV) > 0) {//
                    if (currentV > 0) {
                        open();
                    } else if (currentV < 0) {
                        fold();
                    }
                    cancel();
                    return;
                }

                int currD = topHeight - mContent.getTop();
                int maxD = maxDistance;


                if (currD > maxD / 2) {
                    open();
                } else {
                    fold();
                }
                cancel();

                break;
            case MotionEvent.ACTION_CANCEL:
                cancel();
                break;
        }
    }

    public void open() {
        startScroll(mContent.getTop(), topHeight);
    }

    public void fold() {
        startScroll(mContent.getTop(), topHeight - maxDistance);
    }


    private void move(int dy) {
        int itemHeight = mTopView.getItemHeight();

        int dy1 = getAreaValue(mTopView.getTop(), dy, -mTopView.getItemTop(), 0);
        int dy2 = getAreaValue(mContent.getTop() - topHeight, dy, -(topHeight - itemHeight), 0);

        if (dy1 != 0) {
            ViewCompat.offsetTopAndBottom(mTopView.getView(), dy1);
        }

        if (dy2 != 0) {
            ViewCompat.offsetTopAndBottom(mContent, dy2);
        }
//        onChangeFrameSize();
    }

    @SuppressWarnings("SameParameterValue")
    private int getAreaValue(int top, int dy, int minValue, int maxValue) {
        if (top + dy < minValue) {
            return minValue - top;
        }
        if (top + dy > maxValue) {
            return maxValue - top;
        }
        return dy;
    }

    private void startScroll(int startY, int endY) {
        float distance = endY - startY;
        float t = 1.0F * distance / maxDistance * 800;

        mScroller.startScroll(0, 0, 0, endY - startY, (int) Math.abs(t));
        postInvalidate();
    }

    int oldY = 0;

    @SuppressLint("WrongConstant")
    @Override
    public void computeScroll() {
        super.computeScroll();

        bottomViewTopHeight = mContent.getTop();
        if (mScroller.computeScrollOffset()) {
            isSliding = true;
            int cy = mScroller.getCurrY();
            int dy = cy - oldY;
            move(dy);
            oldY = cy;
            postInvalidate();
        } else {
            oldY = 0;
            isSliding = false;
            stage = mContent.getTop() < mTopView.getMeasuredHeight() ? Stage.FOLD : Stage.OPEN;
            if (mOnChangeStatusListener != null) {
                mOnChangeStatusListener.onChangeStatus(stage);
            }
            postInvalidate();
        }
    }

    public void cancel() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Stage
    public int getStage() {
        if (mContent != null) {
            int top = mContent.getTop();
            if (top < topHeight) {
                return Stage.FOLD;
            } else {
                return Stage.OPEN;
            }
        }
        return stage;
    }


    private OnChangeStatusListener mOnChangeStatusListener;

    public void setOnChangeStatusListener(OnChangeStatusListener listener) {
        this.mOnChangeStatusListener = listener;
    }
}
