package rxtx;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rxtx.exceptions.NotASerialPort;
import rxtx.exceptions.SerialPortOutputStreamCloseFailure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialClient {
    private Logger logger = LoggerFactory.getLogger(SerialClient.class);
    private final int OPEN_PORT_TIMEOUT = 2000; // milli-sec
    private final String RETURN_CHAR = "\n";
    private Config config;

    private Thread readerThread;

    private SerialPort serialPort;

    public SerialClient(Config config) {
        this.config = config;
    }

    public SerialPort openPort(String portName, int bitrate) throws NoSuchPortException, PortInUseException, NotASerialPort, UnsupportedCommOperationException {
        logger.info("Trying to open port {}", portName);
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

        if (portIdentifier.isCurrentlyOwned()) {
            logger.error("Port {} is in use", portName);
            throw new PortInUseException();
        }

        CommPort commPort = portIdentifier.open(this.getClass().getName(), OPEN_PORT_TIMEOUT);

        if (commPort instanceof SerialPort) {
            this.serialPort = (SerialPort) commPort;
            this.serialPort.setSerialPortParams(bitrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            logger.info("Connected to Serial port [name: {}][bitrate: {}]", portName, bitrate);
        } else {
            logger.error("Error: Only serial ports are handled by this example.");
            throw new NotASerialPort();
        }

        return serialPort;
    }

    public void closePort() {
        if (null != serialPort) {
            this.serialPort.close();
            this.serialPort = null;
        }
    }

    /**
     * Record the serial port log into the provided log file
     *
     * @param logFile
     */
    public void recordPortOutput(File logFile) throws IOException {
        logger.info("Serial port logs will be recorded in the file {}", logFile.getAbsolutePath());
        InputStream in = null;
        in = this.serialPort.getInputStream();

        SerialReaderThread sReaderThread = new SerialReaderThread(in, logFile);
        readerThread = new Thread(sReaderThread);
        readerThread.start();
    }

    /**
     * Send data to Serial port
     * @param data
     * @throws SerialPortOutputStreamCloseFailure
     */
    public void writeData(String data) throws SerialPortOutputStreamCloseFailure {
        OutputStream out = null;

        try {
            out = this.serialPort.getOutputStream();
            out.write((data + RETURN_CHAR).getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new SerialPortOutputStreamCloseFailure(e);
                }
            }
        }
    }

    public SerialPort getSerialPort() {
        return this.serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public Thread getReaderThread() {
        return readerThread;
    }

    public void setReaderThread(Thread readerThread) {
        this.readerThread = readerThread;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
