package com.github.argherna.preftool;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Invoked to remove a Preferences node.
 */
public class RemovePreferencesNode implements Callable<Void> {

    private static final System.Logger LOGGER =
            System.getLogger(RemovePreferencesNode.class.getName());

    private Preferences preferences;

    /**
     * Constructs a RemovePreferences instance.
     *
     * @param preferences the Preferences node to remove.
     * @throws NullPointerException if the preferences argument is null.
     */
    public RemovePreferencesNode(Preferences preferences) {
        this.preferences =
                Objects.requireNonNull(preferences, "Preferences to remove cannot be null!");
    }

    /**
     * Removes the Preferences node given in the Constructor and commits the changes immediately.
     *
     * @return This method returns {@code null}.
     * @throws BackingStoreException if a BackingStoreException is thrown.
     * @throws IllegalStateException if an IllegalStateException is thrown.
     * @throws UnsupportedOperationException if an UnsupportedOperationException is thrown.
     *
     * @see Preferences#removeNode()
     * @see Preferences#flush()
     */
    @Override
    public Void call() throws Exception {
        LOGGER.log(System.Logger.Level.INFO, "Removing {0}", preferences);
        preferences.removeNode();
        preferences.flush();
        return null;
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.printf("Usage: %s <nodename-to-remove>%n",
                    RemovePreferencesNode.class.getName());
            System.exit(1);
        }

        var prefs = Preferences.userRoot().node(args[0]);
        var action = new RemovePreferencesNode(prefs);
        try {
            action.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
