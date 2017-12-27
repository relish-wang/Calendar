package com.facebook.react.uimanager.events;

/**
 * @author Relish Wang
 * @since 2017/12/21
 */
public abstract class Event<T> {

    public Event(int viewTag) {
    }

    protected int getViewTag() {
        return 0;
    }

    public abstract String getEventName();

    public abstract void dispatch(RCTEventEmitter rctEventEmitter);
}
