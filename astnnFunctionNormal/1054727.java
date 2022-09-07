class BackupThread extends Thread {
    private CommandLine parseCommandLine(TclObject argv[]) throws ParseException {
        String[] args = new String[argv.length - 1];
        for (int i = 0; i < argv.length - 1; i++) args[i] = argv[i + 1].toString();
        CommandLine cl = (new PosixParser()).parse(this.opts, args);
        return cl;
    }
}
