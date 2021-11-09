package com.github.argherna.preftool;

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

    private final Preferences preferences;

    private final OutputStream outputStream;

    private final boolean nodeOnly;

    /**
     * Constructs an ExportPreferences instance that exports the given node and all its subnodes.
     *
     * <P>
     * Calling this constructor has the same effect as calling
     *
     *
     * <PRE>
     * {
     *     &#64;code
     *     var exportAction = new ExportPreferences(preferences, outputStream, false);
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
     * @param nodeOnly     if true, export only the given Preferences node and no child nodes.
     * @throws NullPointerException if either the Preferences or OutputStream are null.
     */
    public ExportPreferences(Preferences preferences, OutputStream outputStream, boolean nodeOnly) {
        this.preferences =
                Objects.requireNonNull(preferences, "Preferences to export cannot be null!");
        this.outputStream =
                Objects.requireNonNull(outputStream, "Export OutputStream cannot be null!");
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
            preferences.exportNode(outputStream);
        } else {
            preferences.exportSubtree(outputStream);
        }
        return null;
    }
}
