package com.github.argherna.preftool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Invoked to export a Preferences node.
 */
public class ExportPreferences implements Callable<Void> {

    private static final System.Logger LOGGER = System.getLogger(ExportPreferences.class.getName());

    private final Preferences preferences;

    private final OutputStream outputStream;

    private final boolean nodeOnly;

    /**
     * Constructs an ExportPreferences instance that exports the given node and all
     * its subnodes.
     *
     * <P>
     * Calling this constructor has the same effect as calling
     *
     *
     * <PRE>
     * {@code
     * var exportAction = new ExportPreferences(preferences, outputStream, false);
     * }
     * </PRE>
     *
     * @param preferences  the Preferences node to export.
     * @param outputStream the OutputStream to export the Preferences node to.
     */
    public ExportPreferences(Preferences preferences, OutputStream outputStream) {
        this(preferences, outputStream, false);
    }

    /**
     * Constructs an ExportPreferences instance.
     *
     * @param preferences  the Preferences node to export.
     * @param outputStream the OutputStream to export the Preferences node to.
     * @param nodeOnly     if true, export only the given Preferences node and no
     *                     child nodes.
     * @throws NullPointerException if either the Preferences or OutputStream are
     *                              null.
     */
    public ExportPreferences(Preferences preferences, OutputStream outputStream, boolean nodeOnly) {
        this.preferences = Objects.requireNonNull(preferences, "Preferences to export cannot be null!");
        this.outputStream = Objects.requireNonNull(outputStream, "Export OutputStream cannot be null!");
        this.nodeOnly = nodeOnly;
    }

    /**
     * Performs the export.
     *
     * @return This method returns {@code null}.
     * @throws BackingStoreException if a BackingStoreException occurs.
     * @throws IOException           if an IOException occurs.
     *
     * @see Preferences#exportNode(OutputStream)
     * @see Preferences#exportSubtree(OutputStream)
     */
    @Override
    public Void call() throws Exception {
        if (nodeOnly) {
            LOGGER.log(System.Logger.Level.INFO, "Exporting node {0} preferences XML", preferences);
            preferences.exportNode(outputStream);
        } else {
            LOGGER.log(System.Logger.Level.INFO, "Exporting subtree {0} preferences XML", preferences);
            preferences.exportSubtree(outputStream);
        }
        return null;
    }

    /**
     * Command line access to executing export of Preferences XML.
     * 
     * <P>
     * Syntax for invoking this utility is:
     * 
     * <PRE>
     * <CODE>java [options] \
     *     com.github.argherna.preftool.ImportPreferences [options]</CODE>
     * </PRE>
     * 
     * <P>
     * The command line options used are:
     * <DL>
     * <DT><CODE>-o &lt;prefs-xml-file&gt;</CODE>
     * <DD>Output file to write preferences XML to. If not set, preferences XML is
     * written to {@link System#out}.
     * <DT><CODE>-t</CODE>
     * <DD>Export the whole subtree of the named node. If not specified, only the
     * top-level node is exported.
     * </DL>
     * 
     * <P>
     * The command line arguments used are:
     * <DL>
     * <DT><CODE>&lt;nodename&gt;</CODE>
     * <DD>Node to export. Specify as <CODE>/path/to/node</CODE>.
     * </DL>
     * 
     * <P>
     * The system properties you can set are:
     * <DL>
     * <DT><CODE>com.github.argherna.preftool.ExportPreferences.dryRun</CODE>
     * <DD>If <CODE>true</CODE>, do not actually export the preferences XML but
     * print the name of the class, root, node name, and XML file name and exit with
     * status <CODE>2</CODE>.
     * <DT><CODE>com.github.argherna.preftool.ExportPreferences.systemRoot</CODE>
     * <DD>If <CODE>true</CODE>, export the preferences XML under the system root.
     * By default, the XML is exported under the user root.
     * </DL>
     * 
     * @param args command line arguments
     * 
     * @see Preferences
     */
    public static void main(String[] args) {

        if (args.length == 1 && argIsHelpFlag(args[0])) {
            usage();
            System.exit(2);
        }

        var nodeOnly = true;

        OutputStream outstream = System.out;
        var filename = "";
        var nodename = "";

        var argsCount = 0;
        var nextArgIsOptionValue = false;

        while (argsCount < args.length) {
            var arg = args[argsCount];
            if (arg.equals("-o")) {
                nextArgIsOptionValue = true;
            } else if (nextArgIsOptionValue) {
                nextArgIsOptionValue = false;
                filename = arg;
            } else if (arg.equals("-t")) {
                nodeOnly = false;
            } else if (argsCount + 1 == args.length) {
                nodename = arg;
            } else if (!arg.equals("-t") && !arg.equals("-o")) {
                System.err.printf("Unsupported arg \"%s\"%n", arg);
                usage();
                System.exit(1);
            }
            argsCount++;
        }

        var systemRoot = Boolean.getBoolean(ExportPreferences.class.getName() + ".systemRoot");
        var dryRun = Boolean.getBoolean(ExportPreferences.class.getName() + ".dryRun");
        if (dryRun) {
            var root = systemRoot ? "system" : "user";
            var fname = (filename.isBlank()) ? "<System.out>" : filename;
            System.err.printf("%s Dry Run:root=%s,node=%s,filename=%s,nodeOnly=%b%n", ExportPreferences.class.getName(),
                    root, nodename, fname, nodeOnly);
            System.exit(2);
        }

        var preferences = systemRoot ? Preferences.systemRoot().node(nodename) : Preferences.userRoot().node(nodename);
        try {
            if (!filename.isBlank()) {
                outstream = new FileOutputStream(new File(filename));
            }
            var exportPreferencesAction = new ExportPreferences(preferences, outstream, nodeOnly);
            exportPreferencesAction.call();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Prints a usage message to {@link System#err}.
     */
    private static void usage() {
        System.err.printf("Usage: %s [-t -o prefs-xml-file] <nodename>%n", ExportPreferences.class.getName());
    }

    /**
     * Return <CODE>true</CODE> if the given arg is <CODE>-h</CODE>,
     * <CODE>--help</CODE>, or <CODE>-?</CODE>.
     * 
     * @param arg command line argument
     * @return {@code true} if arg is one of the listed values.
     */
    private static boolean argIsHelpFlag(String arg) {
        return arg.equals("-h") || arg.equals("--help") || arg.equals("-?");
    }
}
