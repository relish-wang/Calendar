package wang.relish.datepicker;


import wang.relish.datepicker.drawable.CustomDrawable;

/**
 * MonthView的Item样式
 * Created by 王鑫 on 2017/3/16.
 */
public final class DateStyle {

    /**
     * 日期样式
     */
    private CustomDrawable drawable;
    /**
     * 日期文字
     */
    private String text;
    /**
     * 日期文字大小
     */
    private int textSize;
    /**
     * 日期文字颜色
     */
    private int textColor;

    /**
     * 是否可用
     */
    private boolean enable;

    public DateStyle(String text) {
        this.text = text;
    }

    public CustomDrawable getDrawable() {
        return drawable;
    }

    public void setDrawable(CustomDrawable drawable) {
        this.drawable = drawable;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
