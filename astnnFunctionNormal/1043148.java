class BackupThread extends Thread {
    public static synchronized void copyFile(File src, File dest) throws IOException {
        int tries = 0;
        boolean isError = false;
        do {
            isError = false;
            try {
                FileUtils.copyFile(src, dest);
            } catch (IOException e) {
                isError = true;
            }
            if (isError) Utils.quietlyDelay(250);
            tries++;
        } while (isError && tries <= maxCopyTries);
        if (tries >= maxCopyTries) {
            String msg = String.format("Couldn't copy [%s] to [%s]", src.getPath(), dest.getPath());
            ImageTagTool.logger.error(msg);
            throw new IOException(msg);
        }
    }
}
