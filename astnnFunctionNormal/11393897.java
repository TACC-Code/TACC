class BackupThread extends Thread {
    private void fromFileOutputStream(File absPath) throws FileNotFoundException {
        fileOutputStream = new FileOutputStream(absPath, true);
        fileChannel = fileOutputStream.getChannel();
    }
}
