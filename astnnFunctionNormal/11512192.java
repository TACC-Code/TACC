class BackupThread extends Thread {
    private void readStringChars2(CharArray array, int middle) throws IOException {
        while (true) {
            if (middle >= this.end) {
                array.write(this.buffer, this.start, middle - this.start);
                getMore();
                middle = this.start;
            }
            int ch = this.buffer[middle++];
            if (ch == '"') {
                int len = middle - this.start - 1;
                if (len > 0) {
                    array.write(this.buffer, this.start, len);
                }
                this.start = middle;
                return;
            } else if (ch == '\\') {
                int len = middle - this.start - 1;
                if (len > 0) {
                    array.write(this.buffer, this.start, len);
                }
                this.start = middle;
                array.write(readEscapedChar());
                middle = this.start;
            }
        }
    }
}
