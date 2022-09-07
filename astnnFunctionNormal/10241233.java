class BackupThread extends Thread {
    public int read(int offset, int length) throws IOException {
        int readLength = line.read(buffer, 0, Math.min(buffer.length, length * channels * 2));
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < readLength / channels / 2; j++) {
                int index = j * channels * 2 + i * 2;
                int data = ((int) buffer[index + 1] & 0x000000FF) | (((int) buffer[index]) << 8);
                layer.getChannel(i).setSample(offset + j, data);
            }
        }
        if (readLength >= 0) return readLength / channels / 2; else return readLength;
    }
}
