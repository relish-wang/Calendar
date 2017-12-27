package com.facebook.react.uimanager;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.bridge.ReadableArray;

import java.util.List;
import java.util.Map;

import wang.relish.calendar.react.event.ReactCalendarView;

/**
 * @author Relish Wang
 * @since 2017/12/21
 */
public abstract class ViewGroupManager<T extends ViewGroup> extends ViewManager {
    public abstract String getName();

    protected abstract ReactCalendarView createViewInstance(ThemedReactContext reactContext);

    public abstract void addView(ReactCalendarView parent, View child, int index);

    public abstract void addViews(ReactCalendarView parent, List<View> views);

    public abstract Map<String, Integer> getCommandsMap();

    @SuppressWarnings("ConstantConditions")
    public abstract void receiveCommand(
            ReactCalendarView calendarView,
            int commandType,
            @Nullable ReadableArray array);

    @Nullable
    public abstract Map<String, Object> getExportedCustomDirectEventTypeConstants();
}
