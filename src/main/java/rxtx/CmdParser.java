package rxtx;

import org.apache.commons.cli.*;

import java.util.Optional;

/**
 * Parse the arguments provided in the command line
 */
public class CmdParser {
    private Options opts = new Options();
    private String[] args;
    private Optional<String> configFile = Optional.empty();

    public CmdParser(String[] args) {
        this.args = args;
        this.initOptions();
        this.parse();
    }

    private void initOptions() {
        Option configFileOption = Option.builder("f").longOpt("file").desc("Configuration File Path").hasArgs().build();
        this.opts.addOption(configFileOption);
    }

    private void parse() {
        CommandLineParser clp = new DefaultParser();
        CommandLine cl;

        try {
            cl = clp.parse(this.opts, this.args);
        } catch (ParseException e) {
            throw new IllegalStateException("Failed to parse the arguments.", e);
        }

        String filePath = cl.getOptionValue("file");
        configFile = Optional.ofNullable(filePath);
    }

    public Options getOpts() {
        return opts;
    }

    public void setOpts(Options opts) {
        this.opts = opts;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public Optional<String> getConfigFile() {
        return configFile;
    }

    public void setConfigFile(Optional<String> configFile) {
        this.configFile = configFile;
    }
}
