class BackupThread extends Thread {
    public int execute() throws IOException {
        Process process = processBuilder.start();
        StreamGobbler stdOutGobbler = new StreamGobbler(process.getInputStream());
        StreamGobbler stdErrGobbler = new StreamGobbler(process.getErrorStream());
        stdOutGobbler.start();
        stdErrGobbler.start();
        if (null != is) {
            byte[] buffer = new byte[16384];
            int read = 0;
            OutputStream out = null;
            try {
                out = process.getOutputStream();
                while (true) {
                    read = is.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    out.write(buffer, 0, read);
                }
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                    }
                }
            }
        }
        boolean wait = true;
        int exitValue = 0;
        int retries = 0;
        while (wait && (this.MAXRETRIES == -1 || retries < this.MAXRETRIES)) {
            try {
                exitValue = process.exitValue();
                wait = false;
            } catch (IllegalThreadStateException e) {
                wait = true;
                retries++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        process.destroy();
        while (!stdOutGobbler.isFinished) ;
        while (!stdErrGobbler.isFinished) ;
        this.stdErr = stdErrGobbler.getBuffer();
        this.stdOut = stdOutGobbler.getBuffer();
        return exitValue;
    }
}
