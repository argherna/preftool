package com.github.argherna.preftool;

import static com.github.argherna.preftool.Constants.DRY_RUN;
import static com.github.argherna.preftool.Constants.SYSTEM_ROOT;
import static java.lang.System.Logger.Level.INFO;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Invoked to remove a Preferences node.
 */
public class RemovePreferencesNode implements Callable<Void> {

    private static final System.Logger LOGGER = System.getLogger(RemovePreferencesNode.class.getName());

    private Preferences preferences;

    /**
     * Constructs a RemovePreferences instance.
     *
     * @param preferences the Preferences node to remove.
     * @throws NullPointerException if the preferences argument is null.
     */
    public RemovePreferencesNode(Preferences preferences) {
        this.preferences = Objects.requireNonNull(preferences, "Preferences to remove cannot be null!");
    }

    /**
     * Removes the Preferences node given in the Constructor.
     *
     * @return This method returns {@code null}.
     * @throws BackingStoreException         if a BackingStoreException is thrown.
     * @throws IllegalStateException         if an IllegalStateException is thrown.
     * @throws UnsupportedOperationException if an UnsupportedOperationException is
     *                                       thrown.
     *
     * @see Preferences#removeNode()
     */
    @Override
    public Void call() throws Exception {
        LOGGER.log(INFO, "Removing {0}", preferences);
        preferences.removeNode();
        return null;
    }

    /**
     * Command line access to executing removal of Preferences node.
     * 
     * <P>
     * Syntax for invoking this utility is:
     * <PRE><CODE>java [options] \
     *     com.github.argherna.preftool.RemovePreferencesNode [args]</CODE></PRE>
     * 
     * <P>
     * The command line arguments used are:
     * <DL>
     * <DT><CODE>&lt;nodename-to-remove&gt;</CODE>
     * <DD>Removes preferences node and any associated keys. Specify as
     * <CODE>/path/to/node</CODE>.
     * </DL>
     * 
     * <P>
     * The system properties you can set are:
     * <DL>
     * <DT><CODE>com.github.argherna.preftool.dryRun</CODE>
     * <DD>If <CODE>true</CODE>, do not actually remove the preferences node but
     * print the name
     * of the node to be removed and exit with status <CODE>2</CODE>.
     * <DT><CODE>com.github.argherna.preftool.systemRoot</CODE>
     * <DD>If <CODE>true</CODE>, search for the preferences node to remove under the
     * system root. By default, the user root is searched.
     * </DL>
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.printf("Usage: %s <nodename-to-remove>%n",
                    RemovePreferencesNode.class.getName());
            System.exit(1);
        }

        var node = args[0];
        if (DRY_RUN) {
            var root = SYSTEM_ROOT ? "system" : "user";
            System.err.printf("%s Dry Run: root=%s,node=%s%n",
                    RemovePreferencesNode.class, root, node);
            System.exit(2);
        }

        var prefs = SYSTEM_ROOT ? Preferences.systemRoot().node(node) : Preferences.userRoot().node(node);
        var removeNodeAction = new RemovePreferencesNode(prefs);
        try {
            removeNodeAction.call();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
