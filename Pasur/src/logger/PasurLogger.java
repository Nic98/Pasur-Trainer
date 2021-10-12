package logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Class_definition:
 * The PasurLogger class used to log information to the pasur.log file.
 *
 * @Pattern:
 * Singleton
 *
 * @Class_purpose:
 *
 */
public class PasurLogger {

    private static final String FILE_NAME = "pasur.log";
    private PrintWriter writer;

    private static PasurLogger pasurLogger = null;

    private PasurLogger() {
        try {
            writer = new PrintWriter(new FileWriter(FILE_NAME), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PasurLogger getInstance() {
        if (pasurLogger == null) {
            pasurLogger = new PasurLogger();
        }
        return pasurLogger;
    }

    public void log(String info) {
        writer.println(info);
    }

    public void logProperty(String propertyName, String specInfo) {
        String formatting = String.format("#%s: %s", propertyName, specInfo);
    }
}
