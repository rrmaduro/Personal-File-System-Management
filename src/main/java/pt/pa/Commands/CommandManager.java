package pt.pa.Commands;

import pt.pa.Log.ProcedureLogger;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * The CommandManager class manages the execution, undo, and redo of commands in the system.
 * It maintains stacks of executed commands and undone commands, allowing for navigation and
 * modification of the command history.
 */
public class CommandManager {

    private final Stack<Command> commandLog = new Stack<>();
    private final Stack<Command> undoneCommands = new Stack<>();
    private ProcedureLogger procedureLogger = new ProcedureLogger();

    /**
     * Executes a command and adds it to the command log.
     *
     * @param command The command to be executed.
     * @throws IOException If an I/O error occurs during command execution.
     */
    public void executeCommand(Command command) throws IOException {
        command.execute();
        commandLog.push(command);
        undoneCommands.clear();
        procedureLogger.infoLog("Execute:", command);
    }

    /**
     * Undoes the last executed command.
     *
     * @throws IOException If an I/O error occurs during command undo.
     * @throws EmptyStackException If attempting to undo when the command log is empty.
     */
    public void undoCommand() throws IOException, EmptyStackException {
        if (!commandLog.isEmpty()) {
            Command command = commandLog.pop();
            command.unexecute();
            undoneCommands.push(command);
            procedureLogger.infoLog("Unexecute:", command);
        } else {
            throw new EmptyStackException();
        }
    }

    /**
     * Redoes the last undone command.
     *
     * @throws IOException If an I/O error occurs during command redo.
     * @throws EmptyStackException If attempting to redo when the undone command stack is empty.
     */
    public void redoCommand() throws IOException, EmptyStackException {
        if (!undoneCommands.isEmpty()) {
            Command command = undoneCommands.pop();
            command.execute();
            commandLog.push(command);
            procedureLogger.infoLog("Redo:", command);
        } else {
            throw new EmptyStackException();
        }
    }

    public Stack<Command> getCommandLog() {
        return commandLog;
    }

    public Stack<Command> getUndoneCommands() {
        return undoneCommands;
    }
}
