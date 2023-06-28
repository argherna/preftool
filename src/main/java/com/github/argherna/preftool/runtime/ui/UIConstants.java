package com.github.argherna.preftool.runtime.ui;

import java.util.List;
import java.util.prefs.Preferences;

/**
 * Defines constant values for the user interface classes in this package.
 */
class UIConstants {

    /** List of class names and classes used for Preference types. */
    static final List<Pair<String, Class<?>>> PREF_TYPES = List.of(
            new Pair<String, Class<?>>(String.class.getName(), String.class),
            new Pair<String, Class<?>>("byte[]", byte[].class),
            new Pair<String, Class<?>>(boolean.class.getName(), boolean.class),
            new Pair<String, Class<?>>(double.class.getName(), double.class),
            new Pair<String, Class<?>>(float.class.getName(), float.class),
            new Pair<String, Class<?>>(int.class.getName(), int.class),
            new Pair<String, Class<?>>(long.class.getName(), long.class));
            
    static final int PREF_TEXT_COLUMNS = Preferences.MAX_KEY_LENGTH / 2;
    
    static final int PREF_TEXT_ROWS = 4;

}
