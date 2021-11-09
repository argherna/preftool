package com.github.argherna.preftool;

import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Utilities for operating on Preferences.
 *
 * <P>
 * The name of this class breaks convention; normally this class would have been named "Preferences"
 * but that would be confusing with the JDK's {@linkplain Preferences}.
 */
public class PreferencesUtilities {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private PreferencesUtilities() {
        // empty private constructor
    }

    /**
     * Returns {@code true} if the given value is a double.
     *
     * @param value possible double value as String.
     * @return {@code true} if the value is a double.
     */
    public static boolean isValueDouble(String value) {
        var isValueDouble = false;
        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            isValueDouble = false;
        }
        return isValueDouble;
    }

    /**
     * Returns {@code true} if the given value is a float.
     *
     * @param value possible float value as String.
     * @return {@code true} if the value is a float.
     */
    public static boolean isValueFloat(String value) {
        var isValueFloat = true;
        try {
            Float.parseFloat(value);
        } catch (Exception e) {
            isValueFloat = false;
        }
        return isValueFloat;
    }

    /**
     * Returns {@code true} if the given value is a long.
     *
     * @param value possible long value as String.
     * @return {@code true} if the value is a long.
     */
    public static boolean isValueLong(String value) {
        var isValueLong = true;
        try {
            Long.parseLong(value);
        } catch (Exception e) {
            isValueLong = false;
        }
        return isValueLong;
    }

    /**
     * Returns {@code true} if the given value is an int.
     *
     * @param value possible int value as String.
     * @return {@code true} if the value is an int.
     */
    public static boolean isValueInt(String value) {
        var isValueInt = true;
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            isValueInt = false;
        }
        return isValueInt;
    }

    /**
     * Returns {@code true} if the given value is a boolean.
     *
     * @param value possible boolean value as String.
     * @return {@code true} if the value is a boolean.
     */
    public static boolean isValueBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    /**
     * Returns {@code true} if the given value is a byte array.
     *
     * @param value possible byte array value as String.
     * @return {@code true} if the value is a byte array.
     */
    public static boolean isValueByteArray(Preferences pref, String key) {
        return !Arrays.equals(pref.getByteArray(key, EMPTY_BYTE_ARRAY), EMPTY_BYTE_ARRAY);
    }

    /**
     * Returns the Class of the value for the given key in the given Preferences.
     *
     * @param pref the Preferences node.
     * @param key  the key contained in the Preferences node.
     * @return the Class type of the value.
     */
    public static Class<?> guessType(Preferences pref, String key) {
        Class<?> valueType = String.class;
        var prefValue = pref.get(key, "");
        if (isValueBoolean(prefValue)) {
            valueType = boolean.class;
        } else if (isValueByteArray(pref, key)) {
            valueType = byte[].class;
        } else if (isValueInt(prefValue)) {
            valueType = int.class;
        } else if (isValueLong(prefValue)) {
            valueType = long.class;
        } else if (isValueFloat(prefValue)) {
            valueType = float.class;
        } else if (isValueDouble(prefValue)) {
            valueType = double.class;
        }
        return valueType;
    }

    /**
     *
     * @param preferences Preferences Object.
     * @param key         key whose value is to be retrieved.
     * @param type        the type in the Preferences to retrieve.
     * @return the value with the proper type.
     */
    public static Object getTypedValueFrom(Preferences preferences, String key, Class<?> type) {
        if (type.equals(boolean.class)) {
            return preferences.getBoolean(key, Boolean.FALSE);
        } else if (type.equals(byte[].class)) {
            return preferences.getByteArray(key, EMPTY_BYTE_ARRAY);
        } else if (type.equals(double.class)) {
            return preferences.getDouble(key, 0.0);
        } else if (type.equals(float.class)) {
            return preferences.getFloat(key, 0.0f);
        } else if (type.equals(int.class)) {
            return preferences.getInt(key, 0);
        } else if (type.equals(long.class)) {
            return preferences.getLong(key, 0l);
        } else {
            return preferences.get(key, "(not set)");
        }
    }

    /**
     * Copies the children from {@code source} to {@code destination}.
     *
     * @param source      the source Preferences node whose children and keys are to be copied.
     * @param destination the destination Preferences node.
     * @throws BackingStoreException if a BackingStoreException is thrown.
     */
    public static void copy(Preferences source, Preferences destination)
            throws BackingStoreException {

        // copy keys
        var sourceKeys = source.keys();
        for (var i = 0; i < sourceKeys.length; i++) {
            destination.put(sourceKeys[i], source.get(sourceKeys[i], ""));
        }

        // copy the kids recursively
        var sourceKidsNames = source.childrenNames();
        for (var i = 0; i < sourceKidsNames.length; i++) {
            var destKid = destination.node(sourceKidsNames[i]);
            copy(source.node(sourceKidsNames[i]), destKid);
        }
    }

    /**
     *
     * @param type the type of root node (User or System).
     * @return the Preferences root node of the the appropriate type.
     * @throws IllegalArgumentException if type is not "User" or "System".
     * @see Preferences#systemRoot()
     * @see Preferences#userRoot()
     */
    public static Preferences getPreferencesRoot(String type) {
        if (!type.equals("User") && !type.equals("System")) {
            throw new IllegalArgumentException(String.format(
                    "Preferences root type must be \"User\" or \"System\" (was given \"%s\")",
                    type));
        }
        return type.equals("User") ? Preferences.userRoot() : Preferences.systemRoot();
    }
}
