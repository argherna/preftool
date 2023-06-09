package com.github.argherna.preftool;

import static java.lang.System.Logger.Level.INFO;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

/**
 * Invoked to remove a key and value from a Preferences node.
 */
public class RemovePreferencesKey implements Callable<Void> {

    private static final System.Logger LOGGER = System.getLogger(RemovePreferencesKey.class.getName());

    private final Preferences preferences;

    private final String key;

    /**
     * Construct a new RemovePreferencesKey instance.
     * 
     * @param preferences Preferences to remove the key from.
     * @param key         the key.
     */
    public RemovePreferencesKey(Preferences preferences, String key) {
        this.preferences = Objects.requireNonNull(preferences, "Preferences node cannot be null!");
        this.key = Objects.requireNonNull(key, "Key to remove cannot be null!");
    }

    /**
     * Removes the key and associated value from the Preferences node.
     * 
     * @throws IllegalArgumentException if an IllegalArgumentException is thrown.
     * @throws IllegalStateException    if an IllegalStateException is thrown.
     * @throws NullPointerException     if a NullPointerException is thrown.
     * 
     * @see Preferences#remove(String)
     */
    @Override
    public Void call() throws Exception {
        LOGGER.log(INFO, "Removing key {0} from node {1}", key, preferences);
        preferences.remove(key);
        return null;
    }

    /**
     * Command line access to executing removal of a Preferences key from a node.
     * 
     * <P>
     * Syntax for invoking this utility is:
     * 
     * <PRE>
     * <CODE>java [options] \
     *     com.github.argherna.preftool.RemovePreferencesKey [args]</CODE>
     * </PRE>
     * 
     * <P>
     * The command line arguments used are:
     * <DL>
     * <DT><CODE>&lt;nodename&gt;</CODE>
     * <DD>Preferences node to remove the key from. Specify as
     * <CODE>/path/to/node</CODE>.
     * <DT><CODE>&lt;keyname&gt;</CODE>
     * <DD>Name of the key to remove.
     * </DL>
     * 
     * <P>
     * The system properties you can set are:
     * <DL>
     * <DT><CODE>com.github.argherna.preftool.RemovePreferencesKey.dryRun</CODE>
     * <DD>If <CODE>true</CODE>, do not actually remove the key from the
     * preferences node but print the name of the class, root, node, and key
     * exit with status <CODE>2</CODE>.
     * <DT><CODE>com.github.argherna.preftool.RemovePreferencesKey.systemRoot</CODE>
     * <DD>If <CODE>true</CODE>, search for the preferences node to remove the key
     * from under the system root. By default, the user root is searched.
     * <DT><CODE>com.github.argherna.preftool.RemovePreferencesKey.suppressFlush</CODE>
     * <DD>If <CODE>true</CODE>, do not flush (commit) the changes to the named
     * node. Default action is to flush the change.
     * </DL>
     * 
     * @param args command line arguments
     * 
     * @see Preferences#remove(String)
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.printf("Usage: java %s <nodename> <keyname>%n",
                    RemovePreferencesKey.class.getName());
            System.exit(1);
        }

        var argsCount = 0;
        var node = "";
        var key = "";

        while (argsCount < args.length) {
            var arg = args[argsCount];
            if (argsCount == 0) {
                node = arg;
            } else if (argsCount == 1) {
                key = arg;
            }
            argsCount++;
        }

        var systemRoot = Boolean.getBoolean(RemovePreferencesKey.class.getName() + ".systemRoot");
        var dryRun = Boolean.getBoolean(RemovePreferencesKey.class.getName() + ".dryRun");
        if (dryRun) {
            var root = systemRoot ? "system" : "user";
            System.err.printf("%s Dry Run:root=%s,node=%s,key=%s%n",
                    RemovePreferencesKey.class.getName(), root, node, key);
            System.exit(2);
        }

        var prefs = systemRoot ? Preferences.systemRoot().node(node) : Preferences.userRoot().node(node);
        var removePreferencesKeyAction = new RemovePreferencesKey(prefs, key);
        var suppressFlush = Boolean.getBoolean(RemovePreferencesKey.class.getName() + ".suppressFlush");
        try {
            removePreferencesKeyAction.call();
            if (!suppressFlush) {
                new FlushPreferences(prefs).call();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
