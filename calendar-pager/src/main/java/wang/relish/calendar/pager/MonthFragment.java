package wang.relish.calendar.pager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wang.relish.calendar.MonthAdapter;
import wang.relish.calendar.MonthStyle;
import wang.relish.calendar.OnSelectListener;

/**
 * @author Relish Wang
 * @since 2018/01/21
 */
public class MonthFragment extends Fragment {
    private static final String KEY_MONTH_STYLE = "monthStyle";


    public static MonthFragment newInstance(MonthStyle monthStyle) {
        MonthFragment fragment = new MonthFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MONTH_STYLE, monthStyle);
        fragment.setArguments(bundle);
        return fragment;
    }

    private MonthStyle mMonthStyle;
    private MonthAdapter mAdapter;
    private FoldableMonthView mMonthView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pagerlib_item_month, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMonthStyle = (MonthStyle) arguments.getSerializable(KEY_MONTH_STYLE);
        }
        if (mMonthStyle == null) this.onDestroy();

        mMonthView = view.findViewById(R.id.mv);
        mAdapter = new PMonthAdapter(mMonthStyle);
        mMonthView.setAdapter(mAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mMonthView != null) mMonthView.invalidate();
    }

    public void setOnSelectListener(OnSelectListener listener) {
        if (mMonthView != null) {
            mMonthView.setOnSelectListener(listener);
        }
    }

    public void setOnTopViewChangedListener(ITopView.OnTopViewChangedListener onTopViewChangedListener) {
        if (mMonthView != null) {
            mMonthView.setOnTopViewChangedListener(onTopViewChangedListener);
        }
    }
}
