# preftool

View and manage Java preferences.

## Requirements

This project was developed using [Open JDK 14](https://openjdk.org/projects/jdk/14/). Make sure it or a newer version is downloaded and installed.

## Building

This project uses [maven](https://maven.apache.org/) as its build system. From the project's root directory, run the command below and it will be built.

    mvn clean package

## Installation

Copy `preftool-[VERSION].jar` in the `target` directory to the directory of your choice.

## Running

### Graphical Interface

`preftool` is built on top of [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/) and is run with the command

    java -jar preftool-[VERSION].jar

You'll be presented with a window that has a split pane interface similar to a tool like a file explorer or the Windows Registry Editor. The left pane shows a tree representing the hierarchy of Java Preferences on the computer its run on. The right side shows any keys associated with the node (name of the key, the data type, and the value if it can be represented by text).

The graphical interface lets you view preference nodes and keys and add new nodes and keys. You can export preferences to their [XML format](https://docs.oracle.com/en/java/javase/20/docs/api/java.prefs/java/util/prefs/Preferences.html), and you can import preferences from the same XML format.

### Command Line

There are 6 command line tools, each corresponding to the operations that you can do with the [Preferences API](https://docs.oracle.com/en/java/javase/20/docs/api/java.prefs/java/util/prefs/Preferences.html).

#### AddPreferencesKey

Add a key to a Preferences node. If the node specified doesn't exist, it will be created.

##### Running

Use this command:

    java -cp /path/to/preftool-[VERSION].jar [system properties] \
        com.github.argherna.preftool.AddPreferencesKey \
        [-c <valueclass>] <nodename> <keyname> <value>

###### System Properties

Set these system properties if you want to control how the program is run:

* `com.github.argherna.preftool.dryRun`: Don't add the key or node, but print the values that would be added.
* `com.github.argherna.preftool.suppressFlush`: Don't call [Preferences.flush()](https://docs.oracle.com/en/java/javase/20/docs/api/java.prefs/java/util/prefs/Preferences.html#flush()) after the key and value are added.
* `com.github.argherna.preftool.systemRoot`: The node should be searched for or added to the [System Root](https://docs.oracle.com/en/java/javase/20/docs/api/java.prefs/java/util/prefs/Preferences.html#systemRoot()) Preferences.

###### Arguments

All arguments are required. If any are missing, a usage message will be printed to `System.err` and the program will exit with a status of 1.

* `<nodename>`: path to the node. If it's not in Preferences already, it'll be added.
* `<keyname>`: name of the key.
* `<value>`: value of the key.

###### Options

The `-c <valueclass>` option tells the program which class the value is. You can specify one of these values:

* `boolean` or `bool`
* `byte[]` or `@byte`
* `double` or `d`
* `float` or `f`
* `int` or `i`
* `long` or `l`

The default type is String.

#### AddPreferencesNode

Add a new Preferences node.

##### Running

Use this command:

    java -cp /path/to/preftool-[VERSION].jar [system properties] \
        com.github.argherna.preftool.AddPreferencesNode \
        <nodename-to-add>

###### System Properties

Set these system properties if you want to control how the program is run:

* `com.github.argherna.preftool.dryRun`: Don't add node, but print the values that would be added.
* `com.github.argherna.preftool.suppressFlush`: Don't call Preferences.flush() after the node is added.
* `com.github.argherna.preftool.systemRoot`: The node should be added to the System Root Preferences.

###### Arguments

All arguments are required. If any are missing, a usage message will be printed to `System.err` and the program will exit with a status of 1.

* `<nodename-to-add>`: path to the node. Parents are left alone if they already exist. 

#### ExportPreferences

Export a single Preferences node or a subtree of nodes to XML. 

##### Running

Use this command:

    java -cp /path/to/preftool-[VERSION].jar [system properties] \
        com.github.argherna.preftool.ExportPreferences \
        [-t -o <prefs-xml-file>] <nodename>

###### System Properties

Set these system properties if you want to control how the program is run:

* `com.github.argherna.preftool.dryRun`: Don't export the node(s), but print the values that would be used for the export.
* `com.github.argherna.preftool.systemRoot`: The node should be exported the System Root Preferences.

###### Arguments

All arguments are required. If any are missing, a usage message will be printed to `System.err` and the program will exit with a status of 1.

* `<nodename>`: path to the node to export

###### Options

* `-t`: If set, export the subree under `<nodename>`.
* `-o`: Write XML to a file instead of `System.out`
  * `<prefs-xml-file>`: name of the file to write the Preferences XML to.

#### ImportPreferences

Import Preferences nodes from Preferences XML. 

##### Running

Use this command:

    java -cp /path/to/preftool-[VERSION].jar [system properties] \
        com.github.argherna.preftool.ImportPreferences \
        [-i <prefs-xml-file>]

###### System Properties

Set these system properties if you want to control how the program is run:

* `com.github.argherna.preftool.dryRun`: Don't import the node(s), but print the values that would be used for the import.

###### Options

* `-i`: Read XML from a file instead of `System.in`
  * `<prefs-xml-file>`: name of the file to read the Preferences XML from.

#### RemovePreferencesKey

Remove a key from a Preferences node. The Preferences node specified must exist or the program will report an error.

##### Running

Use this command:

    java -cp /path/to/preftool-[VERSION].jar [system properties] \
        com.github.argherna.preftool.RemovePreferencesKey \
        <nodename> <keyname>

###### System Properties

Set these system properties if you want to control how the program is run:

* `com.github.argherna.preftool.dryRun`: Don't remove the key, but print the values that would be removed.
* `com.github.argherna.preftool.suppressFlush`: Don't call Preferences.flush() after the key is removed.
* `com.github.argherna.preftool.systemRoot`: The node should be searched for from the System Root Preferences.

###### Arguments

All arguments are required. If any are missing, a usage message will be printed to `System.err` and the program will exit with a status of 1.

* `<nodename>`: path to the node. If it's not in Preferences already, the program will not run and an error will be reported.

#### RemovePreferencesNode

Remove an existing Preferences node.

##### Running

Use this command:

    java -cp /path/to/preftool-[VERSION].jar [system properties] \
        com.github.argherna.preftool.RemovePreferencesNode \
        <nodename-to-remove>

###### System Properties

Set these system properties if you want to control how the program is run:

* `com.github.argherna.preftool.dryRun`: Don't add node, but print the values that would be removed.
* `com.github.argherna.preftool.suppressFlush`: Don't call Preferences.flush() after the node is removed.
* `com.github.argherna.preftool.systemRoot`: The node should be removed from the System Root Preferences.

###### Arguments

All arguments are required. If any are missing, a usage message will be printed to `System.err` and the program will exit with a status of 1.

* `<nodename-to-remove>`: path to the node. Parents are left alone if they exist.

## Philosophy

This application is being built entirely with the JDK, without external dependencies. This limits what can be done quickly in terms of developer productivity, but increases user productivity in that building and running the software is faster (which is the main goal).

## Issues

Use the [issues in the GitHub repository](https://github.com/argherna/preftool/issues) to report issues or request features.