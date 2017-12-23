package wang.relish.calendar;

/**
 * MonthView设置触摸回调
 *
 * @author wangxin
 * @since 2017/11/30
 */
/* package */ interface OnSelectListener {
    /**
     * 选择了当前月的某个日期
     *
     * @param monthView 被点击的MonthView
     * @param year      年份
     * @param month     月份
     * @param day       日期
     */
    void onCurrMonthDateSelect(MonthView monthView, int year, int month, int day);

    /**
     * 选择了上个月末尾的某个日期
     *
     * @param monthView 被点击的MonthView
     * @param year      年份
     * @param month     月份
     * @param day       日期
     */
    void onPrevMonthDateSelect(MonthView monthView, int year, int month, int day);

    /**
     * 选择了下个月开头的某个日期
     *
     * @param monthView 被点击的MonthView
     * @param year      年份
     * @param month     月份
     * @param day       日期
     */
    void onNextMonthDateSelect(MonthView monthView, int year, int month, int day);
}