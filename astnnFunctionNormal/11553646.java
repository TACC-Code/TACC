class BackupThread extends Thread {
    public int read(ALayer l, int offset, int length) throws IOException {
        int channels = audioInputStream.getFormat().getChannels();
        int readLength = audioInputStream.read(buffer, 0, length * channels * 2);
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < readLength / channels / 2; j++) {
                int index = j * channels * 2 + i * 2;
                int data = ((int) buffer[index + 1] & 0x000000FF) | (((int) buffer[index]) << 8);
                l.getChannel(i).setSample(offset + j, data);
            }
        }
        if (readLength >= 0) return readLength / channels / 2; else return readLength;
    }
}
