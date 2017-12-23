package android.view;

/**
 * @author Relish Wang
 * @since 2017/12/15
 */
public class ViewGroupHack {
    public static boolean addViewInLayout(ViewGroup viewGroup, View child, int index, ViewGroup.LayoutParams params) {
        return viewGroup.addViewInLayout(child, index, params);
    }

    public static boolean addViewInLayout(ViewGroup viewGroup, View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        return viewGroup.addViewInLayout(child, index, params, preventRequestLayout);
    }
}
