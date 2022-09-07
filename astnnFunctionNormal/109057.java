class BackupThread extends Thread {
    public static byte[] readBytesFromURL(String srcName, String optUpdateMsg) {
        if (!srcName.startsWith("http://") && !srcName.startsWith("file://")) return (null);
        int bufSize = 20000, nBytesRead = 0, nBytesWritten = 0, oByteSize = bufSize;
        byte buf[] = null, oBuf[] = null;
        try {
            buf = new byte[bufSize];
            oBuf = new byte[bufSize];
            if (optUpdateMsg != null) logMsg(optUpdateMsg);
            String sDots = "";
            URL url = new URL(srcName);
            InputStream urlIS = url.openStream();
            logMsg("Reading " + sDots);
            while (true) {
                if (optUpdateMsg != null) {
                    logMsg(".");
                }
                nBytesRead = urlIS.read(buf);
                if (nBytesRead == -1) break; else {
                    if (nBytesRead + nBytesWritten >= oByteSize) {
                        byte tmp[] = new byte[oByteSize + bufSize];
                        for (int i = 0; i < nBytesWritten; i++) tmp[i] = oBuf[i];
                        oBuf = tmp;
                        oByteSize += bufSize;
                    }
                    for (int i = 0; i < nBytesRead; i++) oBuf[nBytesWritten++] = buf[i];
                }
            }
            byte tmp[] = new byte[nBytesWritten];
            for (int i = 0; i < nBytesWritten; i++) tmp[i] = oBuf[i];
            oBuf = tmp;
            if (optUpdateMsg != null) {
                logMsg("\n");
            }
        } catch (Exception e) {
            logMsg("Problem can't readBytesFromURL() e=" + e + "\n");
            return (null);
        }
        return (oBuf);
    }
}
