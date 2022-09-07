class BackupThread extends Thread {
    public static boolean copyFile(File source, File destination, String targetFilename, boolean onlyIfModified, Log logger) throws IOException {
        logger.debug("MvnUtil.copyFile: " + source + ", " + destination + ", " + targetFilename);
        if (onlyIfModified && destination.lastModified() >= source.lastModified()) {
            logger.debug(" * " + targetFilename + " is up to date (" + destination.lastModified() + " >= " + source.lastModified() + ")");
            return false;
        } else {
            FileUtils.copyFile(source.getCanonicalFile(), destination);
            destination.setLastModified(source.lastModified());
            logger.debug(" + " + targetFilename + " has been copied.");
            return true;
        }
    }
}
