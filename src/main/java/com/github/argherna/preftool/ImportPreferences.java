package com.github.argherna.preftool;

import static com.github.argherna.preftool.Constants.DRY_RUN;
import static java.lang.System.Logger.Level.INFO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

/**
 * Invoked to import Preferences from XML.
 */
public class ImportPreferences implements Callable<Void> {

    private static final System.Logger LOGGER = System.getLogger(ImportPreferences.class.getName());

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
     * @throws InvalidPreferencesFormatException if an
     *                                           InvalidPreferencesFormatException
     *                                           occurs.
     * @throws IOException                       if an IOException occurs.
     * @throws SecurityException                 if a SecurityException occurs.
     * @see Preferences#importPreferences(InputStream)
     */
    @Override
    public Void call() throws Exception {
        LOGGER.log(INFO, "Importing preferences");
        Preferences.importPreferences(inputStream);
        return null;
    }

    /**
     * Command line access to executing import of Preferences XML.
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
     * <DT><CODE>-i &lt;prefs-xml-file&gt;</CODE>
     * <DD>Input file to read preferences XML from. If not set, preferences XML is
     * read from {@link System#in}.
     * </DL>
     * 
     * <P>
     * The system properties you can set are:
     * <DL>
     * <DT><CODE>com.github.argherna.preftool.ImportPreferences.dryRun</CODE>
     * <DD>If <CODE>true</CODE>, do not actually import the preferences XML but
     * print the name of the class, root, and XML file name and exit with status
     * <CODE>2</CODE>.
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
        } else if (args.length == 1) {
            System.err.printf("Unsupported arg \"%s\"%n", args[0]);
            usage();
            System.exit(1);
        }

        var instream = System.in;
        var filename = "";

        var argsCount = 0;
        var nextArgIsOptionValue = false;

        while (argsCount < args.length) {
            var arg = args[argsCount];
            if (arg.equals("-i")) {
                nextArgIsOptionValue = true;
            } else if (nextArgIsOptionValue) {
                nextArgIsOptionValue = false;
                filename = arg;
            } else {
                System.err.printf("Unsupported arg \"%s\"%n", arg);
                usage();
                System.exit(1);
            }
            argsCount++;
        }

        if (DRY_RUN) {
            var fname = (filename.isBlank()) ? "<System.in>" : filename;
            System.err.printf("%s Dry Run:filename=%s%n", ImportPreferences.class.getName(), fname);
            System.exit(2);
        }

        try {
            if (!filename.isBlank()) {
                instream = new FileInputStream(new File(filename));
            }
            var importPreferencesAction = new ImportPreferences(instream);
            importPreferencesAction.call();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Prints a usage message to {@link System#err}.
     */
    private static void usage() {
        System.err.printf("Usage: %s [-i prefs-xml-file]%n", ImportPreferences.class.getName());
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
