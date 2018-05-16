package rxtx;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Load the configurations
 */
public class ConfigLoader {
    private Config config;
    private CmdParser cmdParser;
    private File file;
    private ObjectMapper jsonMapper = new ObjectMapper();

    private final static String DEFAULT_CONFIG_FILE = "config.json";

    public ConfigLoader(CmdParser cmdParser) {
        this.cmdParser = cmdParser;
    }

    public void load() {
        Optional<String> configFile = this.cmdParser.getConfigFile();
        String realFilePath;

        if (configFile.isPresent()) {
            realFilePath = configFile.get();
        } else {
            realFilePath = DEFAULT_CONFIG_FILE;
        }

        String finalFilePath = Utils.trans2AbsolutePath(realFilePath);

        this.file = new File(finalFilePath);

        try {
            this.config = jsonMapper.readValue(this.file, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to read config file [%s]", this.file.getAbsolutePath()), e);
        }
    }


    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public CmdParser getCmdParser() {
        return cmdParser;
    }

    public void setCmdParser(CmdParser cmdParser) {
        this.cmdParser = cmdParser;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
