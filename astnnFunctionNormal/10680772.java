class BackupThread extends Thread {
    private void checkPositiveOverflow(int nextWriteCursor, long diff) {
        long boundry = this.writeCursor + diff;
        if (boundry >= QUEUE_SIZE) {
            if ((this.readCursor > this.writeCursor) && (nextWriteCursor < this.readCursor)) {
                this.cleanBufferOnPositiveOverflow(nextWriteCursor);
            } else if ((this.readCursor < this.writeCursor) && (nextWriteCursor >= this.readCursor)) {
                this.cleanBufferOnPositiveOverflow(nextWriteCursor);
            } else {
            }
        } else {
            if ((this.readCursor > this.writeCursor) && (nextWriteCursor >= this.readCursor)) {
                this.cleanBufferOnPositiveOverflow(nextWriteCursor);
            } else {
            }
        }
    }
}
