package com.facebook.react.bridge;

import android.content.Context;

import com.facebook.react.uimanager.UIManagerModule;

/**
 * @author Relish Wang
 * @since 2017/12/21
 */
public abstract class ReactContext extends Context {
    public abstract NativeModule getNativeModule(Class<UIManagerModule> uiManagerModuleClass);
}
