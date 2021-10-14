package logWriter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * PrinterWriter Code learnt from StackOverflow
 * https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it?page=1&tab=votes#tab-top
 * Singleton pattern used.
 * The LogWriter exist
 */
public class LogWriter {
    /**
     * Target auto generated file name
     */
    private static final String LOG_NAME = "pasur.log";

    /**
     * Tool to write text into file
     */
    private PrintWriter printWriter;

    /**
     * Make a singleton LogWriter
     */
    private static LogWriter logger = null;

    /**
     * Constructor for creating a LogWriter
     * It will create a "pasur.log" text file automatically
     * "utf-8" stands for text file
     */
    private LogWriter() {
        try {
            // create a file named "pasur.log" and it contains text
            printWriter = new PrintWriter((LOG_NAME), "utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    };

    /**
     * LogWriter is using Singleton pattern
     * @return itself when calling getLogWriterInstance()
     */
    public static LogWriter getLogWriterInstance() {
        if (logger == null) {
            logger = new LogWriter();
        }
        return logger;
    }

    /**
     * Write text into the generated file "pasur.log"
     * @param text text written
     */
    public void writeLog(String text) {
        printWriter.println(text);
    }

    /**
     * When logging the configurations,
     * the formatting method is used.
     * @param configType Type/Name of the configuration e.g.Seed, Animate, Player0/1 etc.
     * @param configInfo Information included in the configuration e.g. 30006, true/false etc.
     */
    public void writeConfig(String configType, Object configInfo) {
        String stringFiedInfo = String.valueOf(configInfo);
        String config = String.format("#%s: %s", configType, stringFiedInfo);
        writeLog(config);
    }

    /**
     * Close the file when the game ends to prevent "leak"
     */
    public void closeFile() {
        printWriter.close();
    }
}
