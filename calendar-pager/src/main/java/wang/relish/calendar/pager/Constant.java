package wang.relish.calendar.pager;

import wang.relish.calendar.DateStyle;

/**
 * @author Relish Wang
 * @since 2018/01/11
 */
public @interface Constant {
    /**
     * 上月尾和下月首的日期文字颜色
     */
    String UNATTAINABLE_TEXT_COLOR = DateStyle.UNATTAINABLE_TEXT_COLOR;
    /**
     * 选中文字颜色
     */
    String ACTIVE_TEXT_COLOR = DateStyle.ACTIVE_TEXT_COLOR;

    String NORMAL_TEXT_COLOR = DateStyle.NORMAL_TEXT_COLOR;

    String KEY_ACTIVE_DRAWABLE = "active";

    String KEY_SELECTED_DAY = "selectDay";

    String KEY_IS_TODAY = "isToday";

    String KEY_BADGE_NUMBER = "badgeNumber";

    String KEY_IS_SELECTED = "isSelected";

}
