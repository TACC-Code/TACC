class BackupThread extends Thread {
        private final boolean refillBuffer() {
            boolean result = true;
            read_buf_p = 0;
            bytesRead = 0;
            if (isConnected()) {
                if (virtualInput != null) {
                    final byte[] ns = virtualInput.toString().getBytes();
                    int l = ns.length;
                    if (l > read_buf.length) {
                        l = read_buf.length;
                    }
                    bytesRead = l;
                    while (--l >= 0) {
                        read_buf[l] = ns[l];
                    }
                    Generic.debug("Virtual:" + virtualInput.toString());
                    virtualInput = null;
                } else {
                    try {
                        int max = gpsPort.readCheck();
                        if (!stableStrategy || (prevReadCheck == max) || (max > Buffer.C_BUF_SIZE)) {
                            if ((max > Buffer.C_BUF_SIZE)) {
                                prevReadCheck = max - Buffer.C_BUF_SIZE;
                                max = Buffer.C_BUF_SIZE;
                            } else {
                                prevReadCheck = 0;
                            }
                            if (max > 0) {
                                bytesRead = gpsPort.readBytes(read_buf, 0, max);
                            }
                        } else {
                            prevReadCheck = max;
                        }
                    } catch (final Exception e) {
                        bytesRead = 0;
                    }
                }
                if (bytesRead == 0) {
                    result = false;
                } else {
                    if (gpsPort.debugActive()) {
                        final String q = "(" + Generic.getTimeStamp() + ")";
                        gpsPort.writeDebug(q.getBytes(), 0, q.length());
                        gpsPort.writeDebug(read_buf, 0, bytesRead);
                    }
                }
            }
            return result;
        }
}
