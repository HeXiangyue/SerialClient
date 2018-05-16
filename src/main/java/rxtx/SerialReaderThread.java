package rxtx;

import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SerialReaderThread implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(SerialReaderThread.class);
    private InputStream in;
    private File logFile;
    private String stopKeyWord;

    public SerialReaderThread(InputStream in, File logFile) {
        this.in = in;
        this.logFile = logFile;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = this.in.read(buffer)) > -1) {
                if (Thread.currentThread().isInterrupted()) {
                    logger.info("Stop reading data from Serial Port because this is interrupted!");
                    break;
                }

                String data = new String(buffer, 0, len);
                System.out.print(data);
                FileUtils.write(this.logFile, data, true);
            }
        } catch (IOException e) {
            logger.error("Failed to write the logs.", e);
        }
    }

    public String getStopKeyWord() {
        return stopKeyWord;
    }

    public void setStopKeyWord(String stopKeyWord) {
        this.stopKeyWord = stopKeyWord;
    }

}
