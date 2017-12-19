package wang.relish.datepicker.drawable;

import android.graphics.drawable.Drawable;

import java.util.Collections;
import java.util.List;

/**
 * MonthView的Item样式
 * Created by 王鑫 on 2017/3/17.
 */
public abstract class CustomDrawable extends Drawable {
    /**
     * 日期格子的边长
     */
    protected int mCellSize;

    /**
     * 相对MonthView左上角的坐标
     */
    protected int startX, startY;

    /**
     * 设置尺寸
     *
     * @param cellSize 日期格子的边长
     */
    public abstract void setCellSize(int cellSize);

    /**
     * 被放置到MonthView中某个日期的位置
     *
     * @param x x坐标
     * @param y y坐标
     */
    public void setPosition(int x, int y) {
        startX = x;
        startY = y;
    }

    public <T> List<T> findPairingBetween() {
        return Collections.emptyList();
    }

}
