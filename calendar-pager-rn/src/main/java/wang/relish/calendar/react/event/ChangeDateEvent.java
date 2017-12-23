package wang.relish.calendar.react.event;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Locale;

/**
 * @author Relish Wang
 * @since 2017/12/08
 */
public class ChangeDateEvent extends Event<ChangeDateEvent> {
    public static final String EVENT_NAME = "topChangeDate";

    private final int year;
    private final int month;
    private final int day;


    public ChangeDateEvent(int viewTag, int year, int month, int day) {
        super(viewTag);
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap eventData = Arguments.createMap();
        eventData.putString("date", String.format(Locale.ENGLISH, "%d-%02d-%02d", year, month, day));
        return eventData;
    }
}
