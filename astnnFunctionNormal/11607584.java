class BackupThread extends Thread {
    private File createfileHandle(String filePath, boolean overwriteIfExists, boolean append) throws IOException {
        File sinkFile = new File(filePath);
        if (sinkFile.exists()) {
            if (!sinkFile.isFile()) {
                throw new IOException("Attempting to redirect to a path which points to a directory or hidden file.");
            }
            if (overwriteIfExists) {
                if (!sinkFile.delete()) {
                    throw new IOException("The filePath " + filePath + " already contains a file that cannot be deleted.");
                }
            } else if (append) {
                if (!sinkFile.canWrite()) {
                    throw new IOException("Attempting to append to a read-only file: " + filePath);
                }
            } else {
                throw new IOException("There is already a file located at " + filePath + ". " + "The calling code has set overwriteIfExists to false, so this file will not be deleted.");
            }
        }
        return sinkFile;
    }
}
