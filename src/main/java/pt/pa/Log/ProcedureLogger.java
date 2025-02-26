package pt.pa.Log;

import pt.pa.Commands.Command;
import pt.pa.Commands.RemoveCommand;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The ProcedureLogger class provides logging functionality for procedures and exceptions in the application.
 * It supports different logging levels based on the application properties.
 */
public class ProcedureLogger {

    private static final Logger logger = Logger.getLogger(ProcedureLogger.class.getName());
    private Properties appProps = new Properties();

    /**
     * Constructs a ProcedureLogger and initializes logging configurations based on properties files.
     * The logger is configured to append log messages to the "procedure_log.txt" file.
     * The logging level is determined by the "LOGLEVEL" property in the "LogProperties" file.
     */
    public ProcedureLogger() {
        try {
            String logFilePath = Paths.get("src/main/java/pt/pa/Log", "procedure_log.txt").toString();
            Handler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

            String logPropertiesFilePath = Paths.get("src/main/java/pt/pa/Log", "LogProperties").toString();
            appProps.load(new FileInputStream(logPropertiesFilePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs information based on the specified message and command.
     * The logging behavior is determined by the "LOGLEVEL" property in the "LogProperties" file.
     *
     * @param message The message to be logged.
     * @param command The command associated with the log message.
     * @throws IllegalArgumentException If an invalid log property is specified.
     */
    public void infoLog(String message, Command command) {
        String logLevel = (String) appProps.get("LOGLEVEL");
        switch (logLevel) {
            case "FILES":
                if (!(command instanceof RemoveCommand)) {
                    logger.info(message + " " + command);
                }
                break;
            case "DEL":
                if (command instanceof RemoveCommand) {
                    logger.info(message + " " + command);
                }
                break;
            case "ALL":
                logger.info(message + " " + command);
                break;
            default:
                throw new IllegalArgumentException("Invalid log property");
        }
    }
}
