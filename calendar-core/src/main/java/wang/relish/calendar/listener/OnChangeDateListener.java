package wang.relish.calendar.listener;

import android.content.Context;

public interface OnChangeDateListener {

    /**
     * 当选择的日期改变时回调
     *
     * @param context 上下文
     * @param year    年(eg: 2017)
     * @param month   月[1,12]
     * @param day     日[1,31]
     */
    void onChangeDate(Context context, int year, int month, int day);
}