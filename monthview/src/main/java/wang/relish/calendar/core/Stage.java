package wang.relish.calendar.core;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static wang.relish.calendar.core.Stage.FOLD;
import static wang.relish.calendar.core.Stage.OPEN;


/**
 * 展开关闭
 */
@IntDef({OPEN, FOLD})
@Retention(RetentionPolicy.SOURCE)
public @interface Stage {
    int OPEN = 0; //展开
    int FOLD = 1; // 折叠
}