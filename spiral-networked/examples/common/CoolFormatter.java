package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class CoolFormatter extends Formatter {

  private static final Logger logger = Logger.getLogger(CoolFormatter.class.getName());
  
  public static void setup() {
    File loggingConfigurationFile = new File("logging.properties");

    // it only generates the configuration file
    // if it really doesn't exist.
    if (!loggingConfigurationFile.exists()) {
      Writer output = null;
      try {
        output = new BufferedWriter(new FileWriter(loggingConfigurationFile));

        // The configuration file is a property file.
        // The Properties class gives support to
        // define and persist the logging configuration.
        Properties logConf = new Properties();
        logConf.setProperty("handlers", "java.util.logging.FileHandler,"
            + "java.util.logging.ConsoleHandler");
        logConf.setProperty(".level", "INFO");
        logConf.setProperty("java.util.logging.ConsoleHandler.level", "INFO");
        logConf.setProperty("java.util.logging.ConsoleHandler.formatter", "common.CoolFormatter");
        logConf.setProperty("java.util.logging.FileHandler.level", "ALL");
        logConf.setProperty("java.util.logging.FileHandler.pattern", "log/application.log");
        logConf.setProperty("java.util.logging.FileHandler.limit", "50000");
        logConf.setProperty("java.util.logging.FileHandler.count", "1");
        logConf.setProperty("java.util.logging.FileHandler.formatter", "java.util.logging.XMLFormatter");
        logConf.store(output, "Generated");
      } catch (IOException ex) {
        logger.log(Level.WARNING, "Logging configuration file not created", ex);
      } finally {
        try {
          output.close();
        } catch (IOException ex) {
          logger.log(Level.WARNING, "Problems to save "
              + "the logging configuration file in the disc", ex);
        }
      }
    }
    // This is the way to define the system
    // property without changing the command line.
    // It has the same effect of the parameter
    // -Djava.util.logging.config.file
    Properties prop = System.getProperties();
    prop.setProperty("java.util.logging.config.file", "logging.properties");

    // It creates the log directory if it doesn't exist
    // In the configuration file above we specify this
    // folder to store log files:
    // logConf.setProperty(
    // "java.util.logging.FileHandler.pattern",
    // "log/application.log");
    File logDir = new File("log");
    if (!logDir.exists()) {
      logger.info("Creating the logging directory");
      logDir.mkdir();
    }

    // It overwrites the current logging configuration
    // to the one in the configuration file.
    try {
      LogManager.getLogManager().readConfiguration();
    } catch (IOException ex) {
      logger.log(Level.WARNING, "Problems to load the logging " + "configuration file", ex);
    }
  }

  public static void init() {
    setup();
  }

  //
  // Create a DateFormat to format the logger timestamp.
  //
  private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

  private String levelToString(Level level) {
    String blah = level.getName().substring(0, 1);
    if (level == Level.SEVERE) {
      blah += "!!";
    } else if (level == Level.WARNING) {
      blah += "! ";
    }
    return blah;
  }
  
  public String format(LogRecord record) {
    StackTraceElement s = Thread.currentThread().getStackTrace()[7];
    if (s.getClassName().equals("java.util.logging.Logger")) {
      s = Thread.currentThread().getStackTrace()[8];
    }
    StringBuilder builder = new StringBuilder(1000);
    builder.append(df.format(new Date(record.getMillis())));
    builder.append(" [").append(levelToString(record.getLevel())).append("] ");
    builder.append(formatMessage(record));
    builder.append(" [").append(s.getFileName()).append(":").append(s.getLineNumber()).append("]");
    builder.append("\n");
    return builder.toString();
  }

  public String getHead(Handler h) {
    return super.getHead(h);
  }

  public String getTail(Handler h) {
    return super.getTail(h);
  }
}