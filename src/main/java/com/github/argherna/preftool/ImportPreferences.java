package com.github.argherna.preftool;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

/**
 * Invoked to import Preferences from XML.
 */
public class ImportPreferences implements Callable<Void> {

    private final InputStream inputStream;

    /**
     * Construct a new instance of ImportPreferences.
     *
     * @param inputStream the InputStream to read XML from.
     */
    public ImportPreferences(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Performs the import.
     *
     * @return This method returns {@code null}.
     * @throws InvalidPreferencesFormatException if an InvalidPreferencesFormatException occurs.
     * @throws IOException                       if an IOException occurs.
     * @throws SecurityException                 if a SecurityException occurs.
     * @see Preferences#importPreferences(InputStream)
     */
    @Override
    public Void call() throws Exception {
        Preferences.importPreferences(inputStream);
        return null;
    }
}
