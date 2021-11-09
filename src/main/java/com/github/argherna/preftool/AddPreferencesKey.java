package com.github.argherna.preftool;

import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

/**
 * Invoked to add a key and value to a Preferences node.
 */
public class AddPreferencesKey implements Callable<Void> {

    private final Preferences preferences;

    private final String key;

    private final Class<?> type;

    private final Object value;

    /**
     * Construct a new AddPreferencesKey instance.
     *
     * @param preferences Preferences to add the key to.
     * @param key         the key.
     * @param type        the value's type.
     * @param value       the value.
     */
    public AddPreferencesKey(Preferences preferences, String key, Class<?> type, Object value) {
        this.preferences = preferences;
        this.key = key;
        this.type = type;
        this.value = value;
    }

    /**
     * Adds the key and value to the Preferences node.
     *
     * @throws IllegalArgumentException if an IllegalArgumentException is thrown.
     * @throws IllegalStateException    if an IllegalStateException is thrown.
     * @throws NullPointerException     if a NullPointerException is thrown.
     * @throws NumberFormatException    if a NumberFormatException is thrown.
     */
    @Override
    public Void call() throws Exception {
        if (type.equals(boolean.class)) {
            preferences.putBoolean(key, Boolean.valueOf(value.toString()));
        } else if (type.equals(byte[].class)) {
            preferences.putByteArray(key, (byte[]) value);
        } else if (type.equals(double.class)) {
            preferences.putDouble(key, Double.parseDouble(value.toString()));
        } else if (type.equals(float.class)) {
            preferences.putFloat(key, Float.parseFloat(value.toString()));
        } else if (type.equals(int.class)) {
            preferences.putInt(key, Integer.parseInt(value.toString()));
        } else if (type.equals(long.class)) {
            preferences.putLong(key, Long.parseLong(value.toString()));
        } else {
            preferences.put(key, value.toString());
        }
        return null;
    }
}
