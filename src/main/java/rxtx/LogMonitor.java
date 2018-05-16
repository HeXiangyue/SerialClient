package rxtx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogMonitor {
    private final static Logger logger = LoggerFactory.getLogger(LogMonitor.class);
    private long lastTimeFileSize = 0;
    private File logFile;
    private String stopKeyword;

    private boolean needStop = false;
    private ScheduledExecutorService executorService;


    public LogMonitor(File logFile, String stopKeyword) {
        this.logFile = logFile;
        this.stopKeyword = stopKeyword;
    }

    /**
     * Start monitoring the logs until the provided stopKeyword is found
     *
     * @throws FileNotFoundException
     */
    public void start() throws FileNotFoundException {
        this.waitLogPresent();

        final RandomAccessFile randomFile = new RandomAccessFile(this.logFile, "r");

        executorService = Executors.newScheduledThreadPool(1);

        executorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    randomFile.seek(lastTimeFileSize);
                    String tmp = "";
                    while ((tmp = randomFile.readLine()) != null) {
                        String changedData = new String(tmp.getBytes());

                        if (changedData.contains(stopKeyword)) {
                            logger.info("Log monitor will be stopped because stopKeyword found.");
                            needStop = true;
                            break;
                        }
                    }
                    lastTimeFileSize = randomFile.length();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Force stop the log monitor executor
     */
    public void forceStop() {
        logger.info("Stop the log monitor");
        if (null != executorService) {
            executorService.shutdownNow();
        }
    }

    private void waitLogPresent() {
        while (true) {
            logger.info("Waiting log file [{}] to be created to monitor.", logFile.getAbsolutePath());
            if (logFile.exists()) {
                logger.info("Start monitor log file {}", logFile.getAbsolutePath());
                break;
            }

            Utils.sleepInSeconds(2);
        }
    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public String getStopKeyword() {
        return stopKeyword;
    }

    public void setStopKeyword(String stopKeyword) {
        this.stopKeyword = stopKeyword;
    }

    public boolean isNeedStop() {
        return needStop;
    }

    public void setNeedStop(boolean needStop) {
        this.needStop = needStop;
    }
}
