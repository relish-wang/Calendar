package wang.relish.calendar.pager.listener;


import wang.relish.calendar.pager.Stage;

/**
 * @author wangxin
 * @since 2017/12/13
 */
public interface OnChangeStatusListener {
    void onChangeStatus(@Stage int stage);
}