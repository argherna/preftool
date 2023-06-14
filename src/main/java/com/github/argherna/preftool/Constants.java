package com.github.argherna.preftool;

import static java.lang.Boolean.getBoolean;

/**
 * Package level constant values.
 */
class Constants {

    /**
     * System property {@code com.github.argherna.preftool.dryRun}.
     * 
     * <P>
     * Set to {@code true} to activate a run where no changes are made, but print
     * out the changes that would be made if the run was intended to make changes.
     */
    static final Boolean DRY_RUN = getBoolean(Constants.class.getPackageName() + ".dryRun");

    /**
     * System property {@code com.github.argherna.preftool.suppressFlush}.
     * 
     * <P>
     * Set to {@code true} to activate a run where preferences are not flushed after
     * changes (default is to flush all changes).
     */
    static final Boolean SUPPRESS_FLUSH = getBoolean(Constants.class.getPackageName() + ".suppressFlush");

    /**
     * System property {@code com.github.argherna.preftool.systemRoot}.
     * 
     * <P>
     * Set to {@code true} to activate a run where preferences to operate on will be
     * searched from the system root instead of the user root (default is to search
     * user root).
     */
    static final Boolean SYSTEM_ROOT = getBoolean(Constants.class.getPackageName() + ".systemRoot");
}
