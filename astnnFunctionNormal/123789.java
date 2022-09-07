class BackupThread extends Thread {
        private void writeValueTo(OutputStream out, boolean swap) throws IOException {
            InputStream in = parser.getInputStream();
            int len = parser.getReadLength();
            if (swap && (len & 1) != 0) {
                throw new DcmParseException("Illegal length of OW Pixel Data: " + len);
            }
            if (buffer == null) {
                if (swap) {
                    int tmp;
                    for (int i = 0; i < len; ++i, ++i) {
                        tmp = in.read();
                        out.write(in.read());
                        out.write(tmp);
                    }
                } else {
                    for (int i = 0; i < len; ++i) {
                        out.write(in.read());
                    }
                }
            } else {
                byte tmp;
                int c, remain = len;
                while (remain > 0) {
                    c = in.read(buffer, 0, Math.min(buffer.length, remain));
                    if (c == -1) {
                        throw new EOFException("EOF during read of pixel data");
                    }
                    if (swap) {
                        if ((c & 1) != 0) {
                            buffer[c++] = (byte) in.read();
                        }
                        for (int i = 0; i < c; ++i, ++i) {
                            tmp = buffer[i];
                            buffer[i] = buffer[i + 1];
                            buffer[i + 1] = tmp;
                        }
                    }
                    out.write(buffer, 0, c);
                    remain -= c;
                }
            }
            parser.setStreamPosition(parser.getStreamPosition() + len);
        }
}
