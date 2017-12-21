package com.facebook.react.bridge;

import com.facebook.react.uimanager.events.EventDispatcher;

public interface NativeModule {
    EventDispatcher getEventDispatcher();
}
