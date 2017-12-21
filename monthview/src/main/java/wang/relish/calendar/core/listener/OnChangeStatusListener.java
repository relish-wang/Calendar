package wang.relish.calendar.core.listener;


import wang.relish.calendar.core.Stage;

/**
 * @author wangxin
 * @since 2017/12/13
 */
public interface OnChangeStatusListener {
    void onChangeStatus(@Stage int stage);
}