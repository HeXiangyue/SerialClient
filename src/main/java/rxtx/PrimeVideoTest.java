package rxtx;

import com.google.common.base.StandardSystemProperty;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rxtx.exceptions.NotASerialPort;
import rxtx.exceptions.SerialPortOutputStreamCloseFailure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PrimeVideoTest {
    private final static Logger logger = LoggerFactory.getLogger(PrimeVideoTest.class);
    private CmdParser cmdParser;
    private ConfigLoader configLoader;
    private Config config;
    private SerialClient client;
    private LogMonitor logMonitor;
    private File logFile;

    private final static int CASE_STATUS_CHECK_INTERVAL = 10; // seconds


    public PrimeVideoTest(String[] args) {
        this.cmdParser = new CmdParser(args);
        this.configLoader = new ConfigLoader(cmdParser);
        this.configLoader.load();
        this.config = configLoader.getConfig();
        this.client = new SerialClient(config);

        String logFilePath = StandardSystemProperty.USER_DIR.value() +
                StandardSystemProperty.FILE_SEPARATOR.value() +
                config.getLogFile() +
                "_" + System.currentTimeMillis();
        this.logFile = new File(logFilePath);
    }

    /**
     * Record the serial port logs into log file
     *
     * @throws PortInUseException
     * @throws NoSuchPortException
     * @throws NotASerialPort
     * @throws UnsupportedCommOperationException
     * @throws IOException
     */
    public void recordPortOutput() throws PortInUseException, NoSuchPortException, NotASerialPort, UnsupportedCommOperationException, IOException {
        InputStream in = null;
        this.client.openPort(config.getPortName(), config.getBitRate());

        FileUtils.forceDeleteOnExit(logFile);

        this.client.recordPortOutput(logFile);
    }

    public void closeSerialPort() {
        this.client.closePort();
    }

    /**
     * Emulate to run the tests by sending data to Serial port
     *
     * @throws InterruptedException
     * @throws SerialPortOutputStreamCloseFailure
     * @throws FileNotFoundException
     */
    public void runTest() throws InterruptedException, SerialPortOutputStreamCloseFailure, FileNotFoundException {
        this.logMonitor = new LogMonitor(this.logFile, this.config.getStopKeyword());
        this.logMonitor.start();

        Thread.sleep(5000);
        this.client.writeData("3");

        Thread.sleep(5000);
        this.client.writeData("7");
    }

    public void waitTestComplete() {
        long start = System.currentTimeMillis();

        long timeoutMilliSec = config.getCaseTimeout() * 60 * 1000;

        boolean stopWait = false;

        while (! stopWait) {
            long timeLast = System.currentTimeMillis() - start;
            if (timeLast > timeoutMilliSec) {
                logger.warn("Case may not be completed but the timeout is already reached.");
                stopWait = true;
            }else if (this.logMonitor.isNeedStop()) {
                logger.info("Tests complete log is monitored");
                stopWait = true;
            }

            if (stopWait) {
                logger.info("Clean environments...");
                client.getReaderThread().interrupt();
                this.logMonitor.forceStop();
                break;
            }

            logger.info("Test is not completed. Keep waiting...");
            Utils.sleepInSeconds(CASE_STATUS_CHECK_INTERVAL);
        }
    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public static void main(String[] args) throws PortInUseException, NoSuchPortException, NotASerialPort, UnsupportedCommOperationException, SerialPortOutputStreamCloseFailure, InterruptedException, IOException {
        PrimeVideoTest primeVideoTest = new PrimeVideoTest(args);

        logger.info("First Case Start");

        primeVideoTest.recordPortOutput();
        primeVideoTest.runTest();
        primeVideoTest.waitTestComplete();
        primeVideoTest.closeSerialPort();

        logger.info("First Case Done");
    }
}
