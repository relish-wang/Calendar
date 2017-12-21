package wang.relish.calendar.core;

import android.graphics.Color;

import java.io.Serializable;

import wang.relish.calendar.core.drawable.ActiveDrawable;
import wang.relish.calendar.core.drawable.BadgeDrawable;
import wang.relish.calendar.core.drawable.DateDrawable;
import wang.relish.calendar.core.drawable.IDrawable;
import wang.relish.calendar.core.drawable.RedPointDrawable;

/**
 * 日期展示样式
 *
 * @author Relish Wang
 * @since 2017/11/20
 */
public class DateStyle implements Serializable {

    public static final String NORMAL_TEXT_COLOR = "#34393F";
    public static final String UNATTAINABLE_TEXT_COLOR = "#CCCCCC";
    public static final String ACTIVE_TEXT_COLOR = "#FF571A";

    /**
     * 待办事件数
     * 默认不显示
     * 0：不显示
     */
    private int badgeNumber = 0;

    /**
     * 日期文字的颜色
     * 默认: #34393f
     * 上个月或下个月: #CCCCCC
     * 选中: #FF571A
     */
    private String textColor = "#34393f";


    /**
     * 日期
     * eg: 27
     * [1,31]
     */
    private String text = "1";

    /**
     * 是否选中
     */
    private boolean isSelected = false;

    /**
     * 是否是今天
     */
    private boolean isToday = false;


    /**
     * @param text        日期
     * @param badgeNumber 待办事项数量
     * @param isSelected  是否被选中
     * @param isToday     是否是今天
     * @return 日期样式
     */
    public static DateStyle createNormalStyle(String text, int badgeNumber, boolean isSelected, boolean isToday) {
        DateStyle style = new DateStyle(text);
        style.setSelected(isSelected);
        style.setTextColor(isSelected || isToday ? ACTIVE_TEXT_COLOR : NORMAL_TEXT_COLOR);
        style.setBadgeNumber(badgeNumber);
        style.setToday(isToday);
        return style;
    }

    /**
     * 【未选中】的【非当月】日期
     *
     * @param text        日期
     * @param badgeNumber 待办事项数量
     * @return DateStyle
     */
    public static DateStyle createUnattainableStyle(String text, int badgeNumber, boolean isToday) {
        DateStyle style = new DateStyle(text);
        style.setSelected(false);
        style.setTextColor(isToday ? ACTIVE_TEXT_COLOR : UNATTAINABLE_TEXT_COLOR);
        style.setBadgeNumber(badgeNumber);
        style.setToday(isToday);
        return style;
    }


    public DateStyle(String text) {
        this.text = text;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public String getBadgeNumberStr() {
        return badgeNumber <= 0 ? "" : badgeNumber > 99 ? "99+" : badgeNumber + "";
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text + "";
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        this.textColor = isSelected ? ACTIVE_TEXT_COLOR : NORMAL_TEXT_COLOR;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    /**
     * 选中样式
     */
    public IDrawable getActiveDrawable() {
        if (isSelected) return new ActiveDrawable();
        return null;
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

    /**
     * 日期文字样式
     */
    public IDrawable getDateDrawable() {
        return new DateDrawable(text + "", Color.parseColor(textColor), isToday);
    }

    @Override
    public String toString() {
        return "{" +
                "\"badgeNumber\":\'" + badgeNumber + "\"," +
                "\"textColor\":\'" + textColor + "\"," +
                "\"text\":\'" + text + "\"," +
                "\"isSelected\":\'" + isSelected + "\""
                + "}";
    }
}
