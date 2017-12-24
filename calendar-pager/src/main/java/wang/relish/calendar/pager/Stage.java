package wang.relish.calendar.pager;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static wang.relish.calendar.pager.Stage.FOLD;
import static wang.relish.calendar.pager.Stage.OPEN;


/**
 * 展开关闭
 */
@IntDef({OPEN, FOLD})
@Retention(RetentionPolicy.SOURCE)
public @interface Stage {
    int OPEN = 0; //展开
    int FOLD = 1; // 折叠
}