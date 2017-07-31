package wang.relish.datepicker;

import android.graphics.Color;

/**
 * MonthView的整体样式
 *
 * @author Relish Wang
 * @since 2017/3/20
 */
public final class MonthStyle {

    public static final int TODAY_BG_COLOR = Color.parseColor("#F0F0F0");
    /**
     * 选择日期起始终止日的文字颜色
     */
    public static final int ENDS_TEXT_COLOR = Color.parseColor("#FFFFFF");
    /**
     * 选择日期范围内的文字颜色
     */
    public static final int WITH_RANGE_TEXT_COLOR = Color.parseColor("#FFFFFF");
    /**
     * 日期文字颜色
     */
    public static final int ENABLED_TEXT_COLOR = Color.parseColor("#494949");
    /**
     * 今天字体颜色
     */
    public static final int TODAY_TEXT_COLOR = Color.parseColor("#FF9C59");
    /**
     * 未来日期文字颜色
     */
    public static final int DISABLED_TEXT_COLOR = Color.parseColor("#989898");
    /**
     * 选择日期起始终止日的背景颜色
     */
    public static final int ENDS_COLOR = Color.parseColor("#FF571A");
    /**
     * 选择日期范围内的背景颜色
     */
    public static final int WITHIN_RANGE_COLOR = Color.parseColor("#FF571A");
    /**
     * 星期文字的颜色
     */
    public static final int WEEK_TEXT_COLOR = Color.parseColor("#95999C");
    /**
     * 年文字的颜色
     */
    public static final int YEAR_TEXT_COLOR = Color.parseColor("#FF571A");


    public static final int YEAR_TEXT_SIZE = 24;
    public static final int WEEK_TEXT_SIZE = 14;
    public static final int ENABLE_TEXT_SIZE = 14;
    public static final int DISABLE_TEXT_SIZE = 14;


    private int yearTextSize;
    private int yearTextColor;

    private int weekTextSize;
    private int weekTextColor;

    private DateStyle[] dateStyles;

    public int getYearTextSize() {
        return yearTextSize;
    }

    public void setYearTextSize(int yearTextSize) {
        this.yearTextSize = yearTextSize;
    }

    public int getYearTextColor() {
        return yearTextColor;
    }

    public void setYearTextColor(int yearTextColor) {
        this.yearTextColor = yearTextColor;
    }

    public int getWeekTextSize() {
        return weekTextSize;
    }

    public void setWeekTextSize(int weekTextSize) {
        this.weekTextSize = weekTextSize;
    }

    public int getWeekTextColor() {
        return weekTextColor;
    }

    public void setWeekTextColor(int weekTextColor) {
        this.weekTextColor = weekTextColor;
    }

    public DateStyle[] getDateStyles() {
        return dateStyles;
    }

    public void setDateStyles(DateStyle[] dateStyles) {
        this.dateStyles = dateStyles;
    }
}
