package com.github.argherna.preftool;

import static com.github.argherna.preftool.Constants.DRY_RUN;
import static com.github.argherna.preftool.Constants.SUPPRESS_FLUSH;
import static com.github.argherna.preftool.Constants.SYSTEM_ROOT;
import static java.lang.System.Logger.Level.INFO;

import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

/**
 * Invoked to add a Preferences node directly to the root (system or user).
 */
public class AddPreferencesNode implements Callable<Preferences> {

    private static final System.Logger LOGGER = System.getLogger(AddPreferencesNode.class.getName());

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
        LOGGER.log(INFO, "Adding preferences node {0} to {1}",
                path, rootPreferences);
        // This will add the node if it doesn't exist.
        return rootPreferences.node(path);
    }

    /**
     * Command line access to executing an add of a Preferences node.
     * 
     * <P>
     * Syntax for invoking this utility is:
     * 
     * <PRE>
     * <CODE>java [options] \
     *     com.github.argherna.preftool.AddPreferencesNode [args]</CODE>
     * </PRE>
     * 
     * <P>
     * The command line arguments used are:
     * <DL>
     * <DT><CODE>&lt;nodename-to-add&gt;</CODE>
     * <DD>Adds preferences node. Specify as <CODE>/path/to/node</CODE>.
     * </DL>
     * 
     * <P>
     * The system properties you can set are:
     * <DL>
     * <DT><CODE>com.github.argherna.preftool.dryRun</CODE>
     * <DD>If <CODE>true</CODE>, do not actually add the preferences node but
     * print the name of the node to be added and exit with status <CODE>2</CODE>.
     * <DT><CODE>com.github.argherna.preftool.systemRoot</CODE>
     * <DD>If <CODE>true</CODE>, add the preferences node under thesystem root. By
     * default, the preferences node is added to the user root.
     * <DT><CODE>com.github.argherna.preftool.suppressFlush</CODE>
     * <DD>If <CODE>true</CODE>, do not flush (commit) the changes to the named
     * node. Default action is to flush the change.
     * </DL>
     * 
     * @param args command line arguments
     * 
     * @see FlushPreferences
     */
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.printf("Usage: %s <nodename-to-add>", AddPreferencesNode.class.getName());
            System.exit(1);
        }

        var node = args[0];
        if (DRY_RUN) {
            var root = SYSTEM_ROOT ? "system" : "user";
            System.err.printf("%s Dry Run:root=%s,flush=%b,node=%s%n",
                    AddPreferencesNode.class.getName(), root, SUPPRESS_FLUSH, node);
            System.exit(2);
        }

        var rootPreferences = SYSTEM_ROOT ? Preferences.systemRoot() : Preferences.userRoot();
        var addAction = new AddPreferencesNode(rootPreferences, node);
        try {
            var added = addAction.call();
            if (!SUPPRESS_FLUSH) {
                new FlushPreferences(added).call();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
