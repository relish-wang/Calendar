package wang.relish.calendar;

import android.graphics.Color;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期展示样式
 *
 * @author Relish Wang
 * @since 2017/11/20
 */
public class DateStyle extends Attributes implements Serializable {

    public static final String NORMAL_TEXT_COLOR = "#34393F";
    public static final String UNATTAINABLE_TEXT_COLOR = "#CCCCCC";
    public static final String ACTIVE_TEXT_COLOR = "#FF571A";

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
     * 文字大小与屏宽的比例
     */
    private float textSizeScale;

    /**
     * 格子上的样式
     */
    private Map<String, IDrawable> drawables;


    public DateStyle(String text, String textColor) {
        this.text = text;
        this.textColor = textColor;
    }

    public DateStyle(String text, String textColor, float textSizeScale) {
        this.text = text;
        this.textColor = textColor;
        this.textSizeScale = textSizeScale;
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

    public void addDrawable(String key, IDrawable drawable) {
        if (drawables == null) {
            drawables = new HashMap<>();
        }
        drawables.put(key, drawable);
    }

    public IDrawable getDrawable(String key) {
        if (drawables == null) {
            return null;
        }
        return drawables.get(key);
    }

    /**
     * 日期文字样式
     */
    public IDrawable getDateDrawable() {
        return new DateDrawable(text + "", Color.parseColor(textColor), textSizeScale);
    }

    @Override
    public String toString() {
        return "{" +
                "\"textColor\":\'" + textColor + "\"," +
                "\"text\":\"" + text + "\"," +
                "\"ext\":\"" + ext.toString() + "\"" +
                "}";
    }
}
