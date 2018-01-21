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
import wang.relish.calendar.MonthView;
import wang.relish.calendar.OnSelectListener;

/**
 * @author Relish Wang
 * @since 2018/01/21
 */
public class MonthFragment extends Fragment implements OnSelectListener, ITopView.OnTopViewChangedListener {
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
        if (mMonthStyle == null) mMonthStyle = mMonthView.getMonthStyle();

        mMonthView = view.findViewById(R.id.mv);
        mAdapter = new PMonthAdapter(mMonthStyle);
        mMonthView.setAdapter(mAdapter);
        mMonthView.setOnSelectListener(this);
        mMonthView.setOnSelectListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mMonthView != null) mMonthView.invalidate();
    }

    private OnSelectListener mOnSelectListener;

    public void setOnSelectListener(@Nullable OnSelectListener listener) {
        mOnSelectListener = listener;
    }

    private ITopView.OnTopViewChangedListener mOnTopViewChangedListenr;

    public void setOnTopViewChangedListener(@Nullable ITopView.OnTopViewChangedListener listener) {
        mOnTopViewChangedListenr = listener;
    }

    @Override
    public void onCurrMonthDateSelect(MonthView monthView, int year, int month, int day) {
        if (mOnSelectListener != null) {
            mOnSelectListener.onCurrMonthDateSelect(monthView, year, month, day);
        }
    }

    @Override
    public void onPrevMonthDateSelect(MonthView monthView, int year, int month, int day) {
        if (mOnSelectListener != null) {
            mOnSelectListener.onPrevMonthDateSelect(monthView, year, month, day);
        }
    }

    @Override
    public void onNextMonthDateSelect(MonthView monthView, int year, int month, int day) {
        if (mOnSelectListener != null) {
            mOnSelectListener.onNextMonthDateSelect(monthView, year, month, day);
        }
    }

    @Override
    public void onLayoutChanged(ITopView topView) {
        if (mOnTopViewChangedListenr != null) mOnTopViewChangedListenr.onLayoutChanged(topView);
    }
}
