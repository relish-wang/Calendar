package wang.relish.calendar.pager;

import wang.relish.calendar.DateStyle;

/**
 * @author Relish Wang
 * @since 2017/12/23
 */
@Deprecated
public class PDateStyle extends DateStyle {
    public PDateStyle(String text, String textColor) {
        super(text, textColor);
    }

//    public PDateStyle(String text) {
//        super(text);
//    }
//
//
//    /**
//     * 选中样式
//     */
//    public IDrawable getActiveDrawable() {
//        if (isSelected) return new ActiveDrawable();
//        return null;
//    }
//
//    /**
//     * 徽标样式
//     */
//    public IDrawable getBadgeDrawable() {
//        if (badgeNumber > 0) {
//            return isToday ? new BadgeDrawable(getBadgeNumberStr()) : new RedPointDrawable();
//        } else {
//            return null;
//        }
//    }
//
//    public static PDateStyle toPDateStyle(DateStyle dateStyle) {
//        PDateStyle pDateStyle = new PDateStyle(dateStyle.getText());
//        pDateStyle.setBadgeNumber(dateStyle.getBadgeNumber());
//        pDateStyle.setSelected(dateStyle.isSelected());
//        pDateStyle.setTextColor(dateStyle.getTextColor());
//        pDateStyle.setToday(dateStyle.isToday());
//        return pDateStyle;
//    }
}
