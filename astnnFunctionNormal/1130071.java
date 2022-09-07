class BackupThread extends Thread {
    public void testAnimalContext() throws IOException, DataFormatException {
        final Context inContext = ContextSetups.createCompleteAnimalContext();
        final PipedOutputStream out = new PipedOutputStream();
        final Reader in = new InputStreamReader(new PipedInputStream(out));
        final Runnable writeTask = new Runnable() {

            public void run() {
                BurmeisterWriter.writeToBurmeisterFormat(inContext, new PrintStream(out));
            }
        };
        final Thread thread = new Thread(writeTask);
        thread.start();
        final Context resultContext = BurmeisterParser.importBurmeisterFromReader(in);
        assertEquals(inContext.getAttributes().size(), resultContext.getAttributes().size());
        assertEquals(inContext.getObjects().size(), resultContext.getObjects().size());
        assertEquals(inContext.getName(), resultContext.getName());
    }
}
