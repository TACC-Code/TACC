class BackupThread extends Thread {
    protected int getChannels(int streamNumber) {
        int channels = 1;
        if (datum[streamNumber].channel != -1) {
            if (streamNumber % 2 == 1 && datum[streamNumber].channel % 2 == 1 && (datum[streamNumber - 1] != null && datum[streamNumber - 1].channel % 2 == 0)) {
                return -1;
            }
            if (streamNumber % 2 == 0 && datum[streamNumber].channel % 2 == 0 && (datum[streamNumber + 1] != null && datum[streamNumber + 1].channel % 2 == 1)) {
                channels = 2;
            }
        } else {
            if (datum[streamNumber].channels == 2) {
                channels = 2;
            }
        }
        return channels;
    }
}
