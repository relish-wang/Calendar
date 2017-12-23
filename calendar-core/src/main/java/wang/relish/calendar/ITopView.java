package wang.relish.calendar;

import android.view.View;

/**
 * @author Relish Wang
 * @since 2017/11/27
 */
public interface ITopView {

    /**
     * 需要显示的那行的top的y坐标
     *
     * @return y坐标
     */
    int getItemTop();

    /**
     * 需要显示的那行的高度
     *
     * @return 高度
     */
    int getItemHeight();

    void setOnTopViewChangedListener(OnTopViewChangedListener listener);

    /**
     * return this
     *
     * @return View
     */
    View getView();

    //-------------------无需实现,View的方法-----------------------
    int getMeasuredHeight();

    void offsetTopAndBottom(int offset);

    /**
     * Top position of this view relative to its parent.
     *
     * @return The top of this view, in pixels.
     * @see View#getTop()
     */
    int getTop();

    interface OnTopViewChangedListener {
        void onLayoutChanged(ITopView topView);
    }

}
