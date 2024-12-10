import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());

    static {
        try {
            // Console Handler for printing logs to the console
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new SimpleFormatter()); // Basic format for the console
            logger.addHandler(consoleHandler);

            // File Handler for writing logs to a file (app.log)
            FileHandler fileHandler = new FileHandler("app.log", true); // Appends to app.log
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter()); // Basic format for the file
            logger.addHandler(fileHandler);

            // Set logger level to ALL to log all levels (Info, Warning, Severe)
            logger.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println("Error setting up the logger: " + e.getMessage());
        }
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logWarning(String message) {
        logger.warning(message);
    }

    public static void logSevere(String message) {
        logger.severe(message);
    }
}
