class BackupThread extends Thread {
    @Override
    public int runCommand(boolean mergeErrorIntoOutput, List<String> commands) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(mergeErrorIntoOutput);
        Map<String, String> retrievedEnvironment = pb.environment();
        if (overrideAllEnvironment) {
            retrievedEnvironment.clear();
        }
        if (environment != null) {
            retrievedEnvironment.putAll(environment);
        }
        pb.directory(workingDirectory);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Command Line: \n " + pb.command());
        }
        Process process = pb.start();
        final StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), outputFileHandle);
        final StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), errorFileHandle);
        errorGobbler.start();
        outputGobbler.start();
        if (commandInputStream != null) {
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(process.getOutputStream());
                byte[] buf = new byte[BUFFER_SIZE];
                int readLength;
                while ((readLength = commandInputStream.read(buf)) > -1) {
                    bos.write(buf, 0, readLength);
                }
            } finally {
                commandInputStream.close();
                if (bos != null) {
                    bos.close();
                }
            }
        }
        exitStatus = process.waitFor();
        while (outputGobbler.isStillRunning() || (!mergeErrorIntoOutput && errorGobbler.isStillRunning())) {
            LOGGER.debug("The command process " + commands + " is complete, however the output / error 'Gobblers' have not closed their streams yet.  Waiting...");
            Thread.sleep(100);
        }
        if (exceptionThrownByGobbler != null) {
            throw exceptionThrownByGobbler;
        }
        if (outputFileHandle == null) {
            output = outputGobbler.getStreamContent();
        }
        if (mergeErrorIntoOutput || errorFileHandle != null) {
            error = null;
        } else {
            error = errorGobbler.getStreamContent();
        }
        return exitStatus;
    }
}
