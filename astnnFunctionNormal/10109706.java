class BackupThread extends Thread {
    private void copyToDestFile(long checksum) throws IOException {
        RobustFileOutputStream out = new RobustFileOutputStream(directFile, false);
        FileUtils.copyFile(tempOutFile, out);
        long copySum = out.getChecksum();
        if (copySum == checksum) {
            out.close();
            target = directFile;
        } else {
            out.abort();
            throw new IOException("Error writing to " + directFile + " - checksums do not match");
        }
    }
}
