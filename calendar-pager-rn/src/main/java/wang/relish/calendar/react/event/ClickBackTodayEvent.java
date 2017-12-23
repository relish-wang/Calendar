package wang.relish.calendar.react.event;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/**
 * @author Relish Wang
 * @since 2017/12/08
 */
public class ClickBackTodayEvent extends Event<ClickBackTodayEvent> {
    public static final String EVENT_NAME = "topClickBackToday";

    public ClickBackTodayEvent(int viewTag) {
        super(viewTag);
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), Arguments.createMap());
    }
}
