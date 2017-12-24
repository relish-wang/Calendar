package wang.relish.calendar.pager;

import wang.relish.calendar.DateStyle;
import wang.relish.calendar.IDrawable;
import wang.relish.calendar.pager.drawable.BadgeDrawable;
import wang.relish.calendar.pager.drawable.RedPointDrawable;

/**
 * @author Relish Wang
 * @since 2017/12/23
 */
public class PDateStyle extends DateStyle {

    public PDateStyle(String text) {
        super(text);
    }

    /**
     * 徽标样式
     */
    public IDrawable getBadgeDrawable() {
        if (badgeNumber > 0) {
            return isToday ? new BadgeDrawable(getBadgeNumberStr()) : new RedPointDrawable();
        } else {
            return null;
        }
    }

    public static PDateStyle toPDateStyle(DateStyle dateStyle) {
        PDateStyle pDateStyle = new PDateStyle(dateStyle.getText());
        pDateStyle.setBadgeNumber(dateStyle.getBadgeNumber());
        pDateStyle.setSelected(dateStyle.isSelected());
        pDateStyle.setTextColor(dateStyle.getTextColor());
        pDateStyle.setToday(dateStyle.isToday());
        return pDateStyle;
    }
}
