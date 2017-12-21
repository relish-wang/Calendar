package com.facebook.react.bridge;

/**
 * @author Relish Wang
 * @since 2017/12/21
 */
public interface WritableMap<K, V> {
    void putString(K k, V v);

    V get(K k);
}
