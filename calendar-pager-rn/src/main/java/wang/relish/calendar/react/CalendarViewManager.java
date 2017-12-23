package wang.relish.calendar.react;

import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wang.relish.calendar.DateEvent;
import wang.relish.calendar.Utils;
import wang.relish.calendar.react.event.ChangeDateEvent;
import wang.relish.calendar.react.event.ChangePageEvent;
import wang.relish.calendar.react.event.ChangeStatusEvent;
import wang.relish.calendar.react.event.ClickBackTodayEvent;
import wang.relish.calendar.react.event.ReactCalendarView;

/**
 * @author Relish Wang
 * @since 2017/12/07
 */
public class CalendarViewManager extends ViewGroupManager<ReactCalendarView> {
    @Override
    public String getName() {
        return "SRNNativeCalendarView";
    }

    @Override
    protected ReactCalendarView createViewInstance(ThemedReactContext reactContext) {
        return new ReactCalendarView(reactContext);
    }

    /**
     * CalendarView只能有一个child
     */
    private View mChild;

    @Override
    public void addView(ReactCalendarView parent, View child, int index) {
        if (mChild == null) {
            mChild = child;
            parent.addView(child);
            return;
        }
        throw new UnsupportedOperationException(
                "CalendarView only support only one child!:" + mChild + " " + child);
    }

    @Override
    public void addViews(ReactCalendarView parent, List<View> views) {
        for (int i = 0, size = views.size(); i < size; i++) {
            addView(parent, views.get(i), i);
        }
    }

    @ReactProp(name = "dateEvents")
    public void setDataEvents(ReactCalendarView calendarView, ReadableArray array) {
        Map<String, Integer> data = new HashMap<>();
        DateEvent event;
        for (int i = 0; i < array.size(); i++) {
            ReadableMap map = array.getMap(i);
            String date = map.getString("date");
            int eventCount = map.getInt("eventCount");
            event = new DateEventImpl(date, eventCount);

            data.put(event.getDate(), event.getEventCount());
        }
        calendarView.setData(data);
    }

    private static final int COMMAND_SET_DATE_EVENTS = 1;

    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "setDateEvents",
                COMMAND_SET_DATE_EVENTS);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void receiveCommand(
            ReactCalendarView calendarView,
            int commandType,
            @Nullable ReadableArray array) {
        Assertions.assertNotNull(calendarView);
        Assertions.assertNotNull(array);
        switch (commandType) {
            case COMMAND_SET_DATE_EVENTS: {
                setDataEvents(calendarView, array);
                return;
            }
            default:
                throw new IllegalArgumentException(String.format(
                        Locale.ENGLISH,
                        "Unsupported command %d received by %s.",
                        commandType,
                        getClass().getSimpleName()));
        }
    }

    @ReactProp(name = "selectedDate")
    public void selectedDate(ReactCalendarView calendarView, String date) {
        Matcher matcher = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$").matcher(date);
        if (!matcher.matches()) throw new IllegalArgumentException("no such date: " + date);
        String[] split = date.split("-");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        if (!Utils.isDayCorrect(year, month - 1, day)) {
            throw new IllegalArgumentException("no such date: " + date);
        }
        calendarView.selectDate(year, month - 1, day);
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>of(
                ClickBackTodayEvent.EVENT_NAME, MapBuilder.of("registrationName", "onClickBackToday"),
                ChangeDateEvent.EVENT_NAME, MapBuilder.of("registrationName", "onChangeDate"),
                ChangeStatusEvent.EVENT_NAME, MapBuilder.of("registrationName", "onChangeStatus"),
                ChangePageEvent.EVENT_NAME, MapBuilder.of("registrationName", "onChangePage")
        );
    }


    /**
     * @deprecated 此方案无需实现此方法
     */
    @ReactProp(name = "startAnimation")
    public void startAnimation(final ReactCalendarView calendarView, ReadableMap map) {
        final double vy = map.getDouble("vy");
        final double dy = map.getDouble("dy");
    }

    /**
     * @deprecated 此方案无需实现此方法
     */
    @ReactProp(name = "changeAnimation")
    public void changeAnimation(final ReactCalendarView calendarView, ReadableMap map) {
        final double dy = map.getDouble("dy");
    }

    /**
     * @deprecated 此方案无需实现此方法
     */
    @ReactProp(name = "endAnimation")
    public void endAnimation(final ReactCalendarView calendarView, ReadableMap map) {
        final double vy = map.getDouble("vy");
        final double dy = map.getDouble("dy");
    }
}
