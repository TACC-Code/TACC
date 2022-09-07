class BackupThread extends Thread {
    @Override
    public void run() {
        CommandParser parser = CommandProvider.getCommandParser();
        if (parser != null) {
            PipedWriter pwriter = new PipedWriter();
            PipedReader preader = new PipedReader();
            try {
                preader.connect(pwriter);
            } catch (IOException ioe) {
                ErrorManager.getDefault().notify(ioe);
                return;
            }
            PrintWriter printWriter = new PrintWriter(pwriter);
            parser.setOutput(printWriter);
            new CommandInputAdapter(inputTextField, parser, printWriter);
            OutputReader or = new OutputReader(preader);
            readerFuture = Threads.getThreadPool().submit(or);
        } else {
            ErrorManager.getDefault().log(ErrorManager.ERROR, "No CommandParser defined!");
        }
    }
}
