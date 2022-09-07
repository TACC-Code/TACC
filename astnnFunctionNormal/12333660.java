class BackupThread extends Thread {
    protected long copyFile(InputStream sourceStream, OutputStream destStream, int bufferSize, IProgressMonitor monitor, float incrementSize) throws IOException {
        byte[] data = new byte[bufferSize];
        long totalRead = 0;
        int read = 0;
        float stackedWork = 0;
        boolean cancelled = false;
        while (((read = sourceStream.read(data, 0, bufferSize)) > 0) && !cancelled) {
            destStream.write(data, 0, read);
            destStream.flush();
            totalRead += read;
            if (monitor != null) {
                stackedWork += (read * incrementSize);
                if (stackedWork >= 1) {
                    monitor.worked((int) stackedWork);
                    stackedWork = stackedWork - ((int) stackedWork);
                }
                if (monitor.isCanceled()) {
                    cancelled = true;
                }
            }
        }
        return totalRead;
    }
}
