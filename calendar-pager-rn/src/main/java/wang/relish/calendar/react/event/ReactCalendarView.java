package wang.relish.calendar.react.event;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroupHack;
import android.widget.ListView;
import android.widget.ScrollView;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.NativeGestureUtil;

import wang.relish.calendar.CalendarView;
import wang.relish.calendar.Stage;
import wang.relish.calendar.listener.OnChangeDateListener;
import wang.relish.calendar.listener.OnChangePageListener;
import wang.relish.calendar.listener.OnChangeStatusListener;
import wang.relish.calendar.listener.OnClickBackTodayListener;

/**
 * @author Relish Wang
 * @since 2017/12/08
 */
@SuppressLint("ViewConstructor")
public class ReactCalendarView extends CalendarView {

    private final EventDispatcher mEventDispatcher;

    private boolean mDidLayout = false;
    private int mTouchSlop;
    private float mPrevTouchX;
    private boolean mIntercepted;

    public ReactCalendarView(ReactContext reactContext) {
        super(reactContext);
        mTouchSlop = ViewConfiguration.get(reactContext).getScaledTouchSlop();

        mEventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
        setOnClickBackTodayListener(new ClickBackTodayListener());
        setOnChangeDateListener(new ChangeDateListener());
        setOnChangePageListener(new ChangePageListener());
        setOnChangeStatusListener(new ChangeStatusListener());
    }


    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        return ViewGroupHack.addViewInLayout(mContentRootLayout, child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        return ViewGroupHack.addViewInLayout(mContentRootLayout, child, index, params, preventRequestLayout);
    }

    @Override
    public void goToday() {
        super.goToday();
    }

    @Override
    public void selectDate(int year, int month, int day) {
        super.selectDate(year, month, day);
    }

    private View canScroll(View view) {
        if (view instanceof ScrollView || view instanceof ListView) return view;
        if (view.canScrollVertically(+1)) return view;
        if (!(view instanceof ViewGroup)) return null;
        ViewGroup gView = (ViewGroup) view;
        int childCount = gView.getChildCount();
        if (childCount == 0) return null;
        for (int i = 0; i < childCount; i++) {
            View child = gView.getChildAt(i);
            boolean canScrollUp = child.canScrollVertically(+1);//向上
            if (canScrollUp) return gView;
            if (!(child instanceof ViewGroup)) continue;
            ViewGroup gChild = ((ViewGroup) child);
            if (gChild.getChildCount() == 0) continue;
            View canChildScrollUp = canScroll(child);
            if (canChildScrollUp != null) return canChildScrollUp;
        }
        return null;
    }

    private class ClickBackTodayListener implements OnClickBackTodayListener {

        @Override
        public void onClickBackToday() {
            mEventDispatcher.dispatchEvent(new ClickBackTodayEvent(getId()));
        }
    }

    private class ChangeDateListener implements OnChangeDateListener {

        @Override
        public void onChangeDate(Context context, int year, int month, int day) {
            mEventDispatcher.dispatchEvent(new ChangeDateEvent(getId(), year, month, day));
        }
    }

    private class ChangePageListener implements OnChangePageListener {

        @Override
        public void onChangePage(Context context, int year, int month, int day) {
            mEventDispatcher.dispatchEvent(new ChangePageEvent(getId(), year, month, day));
        }
    }

    private class ChangeStatusListener implements OnChangeStatusListener {

        @Override
        public void onChangeStatus(@Stage int stage) {
            mEventDispatcher.dispatchEvent(new ChangeStatusEvent(getId(), stage));
        }
    }


    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!mDidLayout) {
            mDidLayout = true;
        }
    }

    /**
     * {@link CalendarView} overrides {@link ViewGroup#requestDisallowInterceptTouchEvent} and
     * swallows it. This means that any component underneath SwipeRefreshLayout will now interact
     * incorrectly with Views that are above SwipeRefreshLayout. We fix that by transmitting the call
     * to this View's parents.
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (shouldInterceptTouchEvent(ev) && super.onInterceptTouchEvent(ev)) {
            NativeGestureUtil.notifyNativeGestureStarted(this, ev);
            return true;
        }
        return false;
    }

    /**
     * {@link SwipeRefreshLayout} completely bypasses ViewGroup's "disallowIntercept" by overriding
     * {@link ViewGroup#onInterceptTouchEvent} and never calling super.onInterceptTouchEvent().
     * This means that horizontal scrolls will always be intercepted, even though they shouldn't, so
     * we have to check for that manually here.
     */
    private boolean shouldInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevTouchX = ev.getX();
                mIntercepted = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = ev.getX();
                final float xDiff = Math.abs(eventX - mPrevTouchX);

                if (mIntercepted || xDiff > mTouchSlop) {
                    mIntercepted = true;
                    return false;
                }
        }
        return true;
    }
}
