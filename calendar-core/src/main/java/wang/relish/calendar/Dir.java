package wang.relish.calendar;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static wang.relish.calendar.Dir.LEFT;
import static wang.relish.calendar.Dir.NO_MOVEMENT;
import static wang.relish.calendar.Dir.RIGHT;

/**
 * 滑动方向
 *
 * @author wangxin
 * @since 2017/12/1
 */
@IntDef({LEFT, NO_MOVEMENT, RIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface Dir {
    int LEFT = -1;
    int NO_MOVEMENT = 0;
    int RIGHT = 1;
}