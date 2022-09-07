class BackupThread extends Thread {
    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile tempRaf) throws CannotWriteException, IOException {
        FileChannel fc = raf.getChannel();
        int oldTagSize = 0;
        if (tagExists(fc)) {
            if (!canOverwrite(raf)) throw new CannotWriteException("Overwritting of this kind of ID3v2 tag not supported yet");
            fc.position(6);
            ByteBuffer buf = ByteBuffer.allocate(4);
            fc.read(buf);
            oldTagSize = (buf.get(0) & 0xFF) << 21;
            oldTagSize += (buf.get(1) & 0xFF) << 14;
            oldTagSize += (buf.get(2) & 0xFF) << 7;
            oldTagSize += buf.get(3) & 0xFF;
            oldTagSize += 10;
            int newTagSize = tc.getTagLength(tag);
            if (oldTagSize >= newTagSize) {
                fc.position(0);
                fc.write(tc.convert(tag, oldTagSize - newTagSize));
                return;
            }
        }
        fc.position(oldTagSize);
        if (fc.size() > 15 * 1024 * 1024) {
            FileChannel tempFC = tempRaf.getChannel();
            tempFC.position(0);
            tempFC.write(tc.convert(tag, Id3v2TagCreator.DEFAULT_PADDING));
            tempFC.transferFrom(fc, tempFC.position(), fc.size() - oldTagSize);
            fc.close();
        } else {
            ByteBuffer[] content = new ByteBuffer[2];
            content[1] = ByteBuffer.allocate((int) fc.size());
            fc.read(content[1]);
            content[1].rewind();
            content[0] = tc.convert(tag, Id3v2TagCreator.DEFAULT_PADDING);
            fc.position(0);
            fc.write(content);
        }
    }
}
