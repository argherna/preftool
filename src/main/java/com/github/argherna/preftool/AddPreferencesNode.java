package com.github.argherna.preftool;

import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

/**
 * Invoked to add a Preferences node.
 */
public class AddPreferencesNode implements Callable<Preferences> {

    private final String path;

    private final Preferences rootPreferences;

    /**
     * Construct a new AddPreferencesNode instance.
     *
     * @param rootPreferences either the user root or system root.
     * @param path            path of the node to add.
     *
     * @see Preferences#systemRoot()
     * @see Preferences#userRoot()
     */
    public AddPreferencesNode(Preferences rootPreferences, String path) {
        this.rootPreferences = rootPreferences;
        this.path = path;
    }

    /**
     * Performs the add.
     *
     * @return Preferences object that was added.
     * @throws NullPointerException     if a NullPointerException is thrown.
     * @throws IllegalArgumentException if an IllegalArgumentException is thrown.
     * @throws IllegalStateException    if an IllegalStateException is thrown.
     *
     * @see Preferences#node(String)
     */
    @Override
    public Preferences call() throws Exception {
        // This will add the node if it doesn't exist.
        return rootPreferences.node(path);
    }
}
