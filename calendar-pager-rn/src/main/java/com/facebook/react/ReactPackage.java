package com.facebook.react;

import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.List;

/**
 * @author Relish Wang
 * @since 2017/12/21
 */
public interface ReactPackage {

    List<NativeModule> createNativeModules(ReactApplicationContext reactContext);

    List<Class<? extends JavaScriptModule>> createJSModules();

    List<ViewManager> createViewManagers(ReactApplicationContext reactContext);
}
