class BackupThread extends Thread {
    @SuppressWarnings("static-access")
    private void setupOptions() {
        Option help = OptionBuilder.withLongOpt(OPTION_HELP_LONGOPT).withDescription("Display usage information").create(OPTION_HELP_SHORTOPT);
        options.addOption(help);
        nonRunningOptions.addOption(help);
        Option version = OptionBuilder.withLongOpt(OPTION_VERSION_LONGOPT).withDescription("Display version information").create(OPTION_VERSION_SHORTOPT);
        options.addOption(version);
        nonRunningOptions.addOption(version);
        options.addOption(OptionBuilder.withLongOpt(OPTION_DEBUG_LONGOPT).withDescription("Enable debugging of XML and XSL files during transformation").create(OPTION_DEBUG_SHORTOPT));
        options.addOption(OptionBuilder.withLongOpt(OPTION_VERBOSE_OUTPUT_LONGOPT).withDescription("Enable verbose logging output of execution plan").create(OPTION_VERBOSE_OUTPUT_SHORTOPT));
        options.addOption(OptionBuilder.withLongOpt(OPTION_INPUT_LONGOPT).withArgName("file").hasArg().withDescription("QIF input file to convert from").isRequired().create(OPTION_INPUT_SHORTOPT));
        options.addOption(OptionBuilder.withLongOpt(OPTION_OUTPUT_LONGOPT).withArgName("file").hasArg().withDescription("Output file name to write to").isRequired().create(OPTION_OUTPUT_SHORTOPT));
        options.addOption(OptionBuilder.withLongOpt(OPTION_ENCODING_LONGOPT).withArgName("encoding").hasArg().withDescription("Encoding of files to read and write").create());
    }
}
