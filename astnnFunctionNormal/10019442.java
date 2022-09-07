class BackupThread extends Thread {
    public RandomAccessFile delete(RandomAccessFile raf, RandomAccessFile tempRaf) throws IOException {
        FileChannel fc = raf.getChannel();
        fc.position(0);
        if (!tagExists(fc)) return raf;
        fc.position(6);
        ByteBuffer b = ByteBuffer.allocate(4);
        fc.read(b);
        b.rewind();
        int tagSize = (b.get() & 0xFF) << 21;
        tagSize += (b.get() & 0xFF) << 14;
        tagSize += (b.get() & 0xFF) << 7;
        tagSize += b.get() & 0xFF;
        FileChannel tempFC = tempRaf.getChannel();
        tempFC.position(0);
        fc.position(tagSize + 10);
        b = ByteBuffer.allocate(4);
        int skip = 0;
        while (fc.read(b) != -1) {
            if ((b.get(0) & 0xFF) == 0xFF && (b.get(1) & 0xE0) == 0xE0 && (b.get(1) & 0x06) != 0 && (b.get(2) & 0xF0) != 0xF0 && (b.get(2) & 0x08) != 0x08) {
                fc.position(fc.position() - 4);
                break;
            }
            fc.position(fc.position() - 3);
            b.rewind();
            skip++;
        }
        tempFC.transferFrom(fc, 0, fc.size() - tagSize - 10 - skip);
        return tempRaf;
    }
}
