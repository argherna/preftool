package com.github.argherna.preftool;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

/**
 * Invoked to flush preferences immediately.
 */
/*
 * There is no command line executable associated with this action because it
 * would require an operation to be performed before the flush is called.
 * Calling a flush on its own is a no-op.
 */
public class FlushPreferences implements Callable<Void> {

    private static final System.Logger LOGGER = System.getLogger(FlushPreferences.class.getName());

    private final Preferences preferences;

    /**
     * Constructs a FlushPreferences instance.
     * 
     * @param preferences Preferences to be flushed.
     */
    public FlushPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    /**
     * Flushes the given preferences to the backing store.
     * 
     * @throws BackingStoreException if a BackingStoreException is thrown.
     * 
     * @see Preferences#flush()
     */
    @Override
    public Void call() throws Exception {
        LOGGER.log(System.Logger.Level.INFO, "Flushing {0}", preferences);
        preferences.sync();
        preferences.flush();
        return null;
    }

}
