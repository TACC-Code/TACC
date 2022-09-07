class BackupThread extends Thread {
        public void handler(int offset, int data) {
            if ((data & 0x80) != 0) {
                if ((popeye_videoram.read(offset) & 0xf0) != ((data << 4) & 0xf0)) {
                    dirtybuffer2[offset] = 1;
                    popeye_videoram.write(offset, (popeye_videoram.read(offset) & 0x0f) | ((data << 4) & 0xf0));
                }
            } else {
                if ((popeye_videoram.read(offset) & 0x0f) != (data & 0x0f)) {
                    dirtybuffer2[offset] = 1;
                    popeye_videoram.write(offset, (popeye_videoram.read(offset) & 0xf0) | (data & 0x0f));
                }
            }
        }
}
