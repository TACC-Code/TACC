class BackupThread extends Thread {
    public Stack(VolumeReader reader) {
        width = reader.getWidth();
        height = reader.getHeight();
        depth = reader.getDepth();
        writer = reader;
    }
}
