class BackupThread extends Thread {
        private int getSampleSizeInBytes() {
            return getFormat().getFrameSize() / getFormat().getChannels();
        }
}
