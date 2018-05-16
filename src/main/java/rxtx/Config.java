package rxtx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Pojo for the configuration
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    private String portName;
    private int bitRate;
    private int playbackDuration;
    private String logFile;
    private String stopKeyword;
    @JsonProperty("caseTimeoutMinutes")
    private int caseTimeout;


    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getPlaybackDuration() {
        return playbackDuration;
    }

    public void setPlaybackDuration(int playbackDuration) {
        this.playbackDuration = playbackDuration;
    }


    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getStopKeyword() {
        return stopKeyword;
    }

    public void setStopKeyword(String stopKeyword) {
        this.stopKeyword = stopKeyword;
    }

    public int getCaseTimeout() {
        return caseTimeout;
    }

    public void setCaseTimeout(int caseTimeout) {
        this.caseTimeout = caseTimeout;
    }
}
