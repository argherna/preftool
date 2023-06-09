package com.github.argherna.preftool;

import static java.lang.System.Logger.Level.INFO;

import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

/**
 * Invoked to add a key and value to a Preferences node.
 */
public class AddPreferencesKey implements Callable<Void> {

    private static final System.Logger LOGGER = System.getLogger(AddPreferencesKey.class.getName());

    private final Preferences preferences;

    private final String key;

    private final Class<?> type;

    private final Object value;

    /**
     * Construct a new AddPreferencesKey instance.
     *
     * @param preferences Preferences to add the key to.
     * @param key         the key.
     * @param type        the value's type.
     * @param value       the value.
     */
    public AddPreferencesKey(Preferences preferences, String key, Class<?> type, Object value) {
        this.preferences = preferences;
        this.key = key;
        this.type = type;
        this.value = value;
    }

    /**
     * Adds the key and value to the Preferences node.
     *
     * @throws IllegalArgumentException if an IllegalArgumentException is thrown.
     * @throws IllegalStateException    if an IllegalStateException is thrown.
     * @throws NullPointerException     if a NullPointerException is thrown.
     * @throws NumberFormatException    if a NumberFormatException is thrown.
     * 
     * @see Preferences#put()
     * @see Preferences#putBoolean()
     * @see Preferences#putByteArray()
     * @see Preferences#putDouble()
     * @see Preferences#putFloat()
     * @see Preferences#putInt()
     * @see Preferences#putLong()
     * @see Preferences#flush()
     */
    @Override
    public Void call() throws Exception {
        LOGGER.log(INFO, "Adding key {0} to {1}", key, preferences);
        if (type.equals(boolean.class)) {
            preferences.putBoolean(key, Boolean.valueOf(value.toString()));
        } else if (type.equals(byte[].class)) {
            preferences.putByteArray(key, (byte[]) value);
        } else if (type.equals(double.class)) {
            preferences.putDouble(key, Double.parseDouble(value.toString()));
        } else if (type.equals(float.class)) {
            preferences.putFloat(key, Float.parseFloat(value.toString()));
        } else if (type.equals(int.class)) {
            preferences.putInt(key, Integer.parseInt(value.toString()));
        } else if (type.equals(long.class)) {
            preferences.putLong(key, Long.parseLong(value.toString()));
        } else {
            preferences.put(key, value.toString());
        }
        return null;
    }

    /**
     * Command line access to executing adding a Preferences key to a node.
     * 
     * <P>
     * Syntax for invoking this utility is:
     * 
     * <PRE>
     * <CODE>java [options] \
     *     com.github.argherna.preftool.AddPreferencesKey [options] [args]</CODE>
     * </PRE>
     * 
     * <P>
     * The command line options used are:
     * <DL>
     * <DT><CODE>-c &lt;value-class&gt;</CODE>
     * <DD>Name of the value class to use (either the short name or long name). One
     * of the following:
     * <TABLE>
     * <TH>
     * <TD>Long Name</TD>
     * <TD>Short Name</TD>
     * <TD>Description</TD></TH>
     * <TR>
     * <TD><CODE>boolean</CODE></TD>
     * <TD><CODE>bool</CODE></TD>
     * <TD>boolean value (key value must parse to <CODE>true</CODE>, otherwise
     * <CODE>false</CODE> will be stored).</TD>
     * </TR>
     * <TR>
     * <TD><CODE>byte[]</CODE></TD>
     * <TD><CODE>@byte</CODE></TD>
     * <TD>byte array (hard to specify on the command line--need to figure this one
     * out).</TD>
     * </TR>
     * <TR>
     * <TD><CODE>double</CODE></TD>
     * <TD><CODE>d</CODE></TD>
     * <TD>double value.</TD>
     * </TR>
     * <TR>
     * <TD><CODE>float</CODE></TD>
     * <TD><CODE>f</CODE></TD>
     * <TD>float value.</TD>
     * </TR>
     * <TR>
     * <TD><CODE>int</CODE></TD>
     * <TD><CODE>i</CODE></TD>
     * <TD>int value.</TD>
     * </TR>
     * <TR>
     * <TD><CODE>long</CODE></TD>
     * <TD><CODE>l</CODE></TD>
     * <TD>long value.</TD>
     * </TR>
     * </TABLE>
     * </DL>
     * If not specified, <CODE>String</CODE> is used.
     * 
     * <P>
     * The command line arguments used are:
     * <DL>
     * <DT><CODE>&lt;nodename&gt;</CODE>
     * <DD>Preferences node to add the key to. It is created if it doesn't exist.
     * Specify as <CODE>/path/to/node</CODE>.
     * <DT><CODE>&lt;keyname&gt;</CODE>
     * <DD>Name of the key to add.
     * <DT><CODE>&lt;value&gt;</CODE>
     * <DD>Value to store.
     * </DL>
     * 
     * <P>
     * The system properties you can set are:
     * <DL>
     * <DT><CODE>com.github.argherna.preftool.AddPreferencesKey.dryRun</CODE>
     * <DD>If <CODE>true</CODE>, do not actually add the key and value to the
     * preferences node but print the name of the class, root, node, key, and value
     * and exit with status <CODE>2</CODE>.
     * <DT><CODE>com.github.argherna.preftool.AddPreferencesKey.systemRoot</CODE>
     * <DD>If <CODE>true</CODE>, search for the preferences node to add the key to
     * under the system root. By default, the user root is searched.
     * <DT><CODE>com.github.argherna.preftool.AddPreferencesKey.suppressFlush</CODE>
     * <DD>If <CODE>true</CODE>, do not flush (commit) the changes to the named
     * node. Default action is to flush the change.
     * </DL>
     * 
     * @param args command line arguments
     * 
     * @see Boolean#parseBoolean(String)
     * @see Double#parseDouble(String)
     * @see Float#parseFloat(String)
     * @see Integer#parseInt(String)
     * @see Long#parseLong(String)
     * @see Preferences#put()
     * @see Preferences#putBoolean()
     * @see Preferences#putByteArray()
     * @see Preferences#putDouble()
     * @see Preferences#putFloat()
     * @see Preferences#putInt()
     * @see Preferences#putLong()
     */
    public static void main(String[] args) {

        if (args.length < 3) {
            System.err.printf("Usage: %s [-c <valueclass>] <nodename> <keyname> <value>%n",
                    AddPreferencesKey.class.getName());
            System.exit(1);
        }

        Class<?> type = String.class;
        var node = "";
        var key = "";
        var value = "";

        var argsCount = 0;
        var nextArgIsOptionValue = false;
        while (argsCount < args.length) {
            var arg = args[argsCount];
            if (arg.equals("-c")) {
                nextArgIsOptionValue = true;
            } else {
                if (nextArgIsOptionValue) {
                    try {
                        type = checkClassType(arg);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    }
                    nextArgIsOptionValue = false;
                } else {
                    if (argsCount == 0 || (argsCount == 2 && node.equals(""))) {
                        node = arg;
                    } else if (argsCount == 1 || argsCount == 3) {
                        key = arg;
                    } else if (argsCount == 2 || argsCount == 4) {
                        value = arg;
                    }
                }
            }
            argsCount++;
        }

        var systemRoot = Boolean.getBoolean(AddPreferencesKey.class.getName() + ".systemRoot");
        var dryRun = Boolean.getBoolean(AddPreferencesKey.class.getName() + ".dryRun");
        if (dryRun) {
            var root = systemRoot ? "system" : "user";
            System.err.printf("%s Dry Run: root=%s,type=%s,node=%s,key=%s,value=%s%n",
                    AddPreferencesKey.class, root, type.toString(), node, key, value);
            System.exit(2);
        }

        var prefs = systemRoot ? Preferences.systemRoot().node(node) : Preferences.userRoot().node(node);
        var addPreferencesKeyAction = new AddPreferencesKey(prefs, key, type, value);
        var suppressFlush = Boolean.getBoolean(AddPreferencesKey.class.getName() + ".suppressFlush");
        try {
            addPreferencesKeyAction.call();
            if (!suppressFlush) {
                new FlushPreferences(prefs).call();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Checks the given classname for one of the following as described in
     * {@link #main(String[])} and returns the associated class type or throws and
     * IllegalArgumentException if the classname is not one of what was expected.
     * 
     * @param classname name of the class type to return.
     * @return class type of the given classname.
     * @throws IllegalArgumentException if classname is not one of the expected
     *                                  types.
     */
    private static Class<?> checkClassType(String classname) {
        var checkFailed = true;
        Class<?> cls = String.class;
        if (classname.equals("boolean") || classname.equals("bool")) {
            checkFailed = false;
            cls = boolean.class;
        }

        if (classname.equals("byte[]") || classname.equals("@byte")) {
            checkFailed = false;
            cls = byte[].class;
        }

        if (classname.equals("double") || classname.equals("d")) {
            checkFailed = false;
            cls = double.class;
        }

        if (classname.equals("float") || classname.equals("f")) {
            checkFailed = false;
            cls = float.class;
        }

        if (classname.equals("int") || classname.equals("i")) {
            checkFailed = false;
            cls = int.class;
        }

        if (classname.equals("long") || classname.equals("l")) {
            checkFailed = false;
            cls = long.class;
        }

        if (checkFailed) {
            throw new IllegalArgumentException(String.format(
                    "Expected one of class names boolean, byte[], double, float, int, long, or String for class type. Got %s",
                    classname));
        } else {
            return cls;
        }
    }
}
