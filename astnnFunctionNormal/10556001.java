class BackupThread extends Thread {
    public DeveloperCLIContext(DeveloperCLI cli, Reader reader, PrintStream writer) {
        super(cli);
        this.reader = reader;
        this.writer = writer;
    }
}
