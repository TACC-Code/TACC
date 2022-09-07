class BackupThread extends Thread {
    private void fromRandomAccessFile(File absPath) throws FileNotFoundException {
        String rafMode = "r" + (writing ? "w" : "");
        if (plus && reading && !absPath.isFile()) {
            writing = false;
            throw new FileNotFoundException("");
        }
        file = new RandomAccessFile(absPath, rafMode);
        fileChannel = file.getChannel();
    }
}
