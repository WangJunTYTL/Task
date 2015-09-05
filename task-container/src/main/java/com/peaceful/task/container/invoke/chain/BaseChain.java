package com.peaceful.task.container.invoke.chain;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public class BaseChain implements Chain {


    // ----------------------------------------------------------- Constructors



    public BaseChain() {

    }


    public BaseChain(Command command) {

        addCommand(command);

    }



    public BaseChain(Command[] commands) {

        if (commands == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < commands.length; i++) {
            addCommand(commands[i]);
        }

    }


    public BaseChain(Collection commands) {

        if (commands == null) {
            throw new IllegalArgumentException();
        }
        Iterator elements = commands.iterator();
        while (elements.hasNext()) {
            addCommand((Command) elements.next());
        }

    }


    // ----------------------------------------------------- Instance Variables


    protected Command[] commands = new Command[0];




    // ---------------------------------------------------------- Chain Methods


    public void addCommand(Command command) {

        if (command == null) {
            throw new IllegalArgumentException();
        }
        Command[] results = new Command[commands.length + 1];
        System.arraycopy(commands, 0, results, 0, commands.length);
        results[commands.length] = command;
        commands = results;

    }


    public boolean execute(Context context) throws Exception {

        // Verify our parameters
        if (context == null) {
            throw new IllegalArgumentException();
        }


        // Execute the commands in this list until one returns true
        // or throws an exception
        boolean saveResult = false;
        Exception saveException = null;
        int i = 0;
        int n = commands.length;
        for (i = 0; i < n; i++) {
            try {
                saveResult = commands[i].execute(context);
                if (saveResult) {
                    break;
                }
            } catch (Exception e) {
                saveException = e;
                break;
            }
        }

        // Return the exception or result state from the last execute()
        if ((saveException != null)  ) {
            throw saveException;
        } else {
            return (saveResult);
        }

    }

    /**
     * <p>Return an array of the configured {@link Command}s for this
     * {@link Chain}.  This method is package private, and is used only
     * for the unit tests.</p>
     */
    Command[] getCommands() {

        return (commands);

    }


}
