package wang.relish.calendar.listener;


import wang.relish.calendar.Stage;

/**
 * @author wangxin
 * @since 2017/12/13
 */
public interface OnChangeStatusListener {
    void onChangeStatus(@Stage int stage);
}