package wang.relish.calendar.react.event;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import wang.relish.calendar.core.Stage;

/**
 * @author Relish Wang
 * @since 2017/12/11
 */
public class ChangeStatusEvent extends Event<ChangeStatusEvent> {

    public static final String EVENT_NAME = "topChangeStatus";

    @Stage
    private int stage;

    public ChangeStatusEvent(int viewTag, @Stage int stage) {
        super(viewTag);
        this.stage = stage;
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
        eventData.putString("type", stage == Stage.OPEN ? "month" : "week");
        return eventData;
    }
}
