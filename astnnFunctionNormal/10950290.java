class BackupThread extends Thread {
    void write(OutputBitStream stream) throws IOException {
        stream.writeUnsignedBits(spreadMethod, 2);
        stream.writeUnsignedBits(interpolationMethod, 2);
        int count = gradientRecords.length;
        stream.writeUnsignedBits(count, 4);
        for (int i = 0; i < count; i++) {
            gradientRecords[i].write(stream);
        }
    }
}
