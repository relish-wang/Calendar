package wang.relish.calendar;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Relish Wang`
 * @since 2018/01/09
 */
/* package */class Attributes {

    private Map<String, Object> ext;

    public void setAttribute(@NonNull String key, boolean value) {
        if (ext == null) ext = new HashMap<>();
        ext.put(key, value);
    }

    public void setAttribute(@NonNull String key, int value) {
        if (ext == null) ext = new HashMap<>();
        ext.put(key, value);
    }

    public void setAttribute(@NonNull String key, long value) {
        if (ext == null) ext = new HashMap<>();
        ext.put(key, value);
    }

    public void setAttribute(@NonNull String key, String value) {
        if (ext == null) ext = new HashMap<>();
        ext.put(key, value);
    }

    public void setAttribute(@NonNull String key, JSONObject value) {
        if (ext == null) ext = new HashMap<>();
        ext.put(key, value);
    }

    public boolean getBooleanAttribute(@NonNull String key) {
        return getBooleanAttribute(key, false);
    }

    public boolean getBooleanAttribute(@NonNull String key, boolean defVal) {
        if (ext == null || ext.size() == 0) return defVal;
        Object o = ext.get(key);
        if (o instanceof Boolean) {
            return (boolean) o;
        }
        return defVal;
    }

    public int getIntAttribute(@NonNull String key) {
        return getIntAttribute(key, 0);
    }

    public int getIntAttribute(@NonNull String key, int defVal) {
        if (ext == null || ext.size() == 0) return defVal;
        Object o = ext.get(key);
        if (o instanceof Integer) {
            return (int) o;
        }
        return defVal;
    }

    public long getLongAttribute(@NonNull String key) {
        return getLongAttribute(key, 0L);
    }

    public long getLongAttribute(@NonNull String key, long defVal) {
        if (ext == null || ext.size() == 0) return defVal;
        Object o = ext.get(key);
        if (o instanceof Long) {
            return (long) o;
        }
        return defVal;
    }

    public String getStringAttribute(@NonNull String key) {
        return getStringAttribute(key, null);
    }

    public String getStringAttribute(@NonNull String key, String defVal) {
        if (ext == null || ext.size() == 0) return defVal;
        Object o = ext.get(key);
        if (o instanceof String) {
            return (String) o;
        }
        return defVal;
    }

    public JSONObject getJSONObjectAttribute(@NonNull String key) {
        if (ext == null || ext.size() == 0) return null;
        Object o = ext.get(key);
        if (o instanceof JSONObject) {
            return (JSONObject) o;
        }
        return null;
    }
}
