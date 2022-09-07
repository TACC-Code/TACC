class BackupThread extends Thread {
    public static boolean copyFile(String srcName, String dstName, String optUpdateMsg, int optEstInputFileLth) {
        try {
            FileOutputStream dstFOS = new FileOutputStream(new File(dstName));
            FileInputStream srcFIS = null;
            int bufSize = 100000, nBytesRead = 0, nBytesWritten = 0;
            byte buf[] = new byte[bufSize];
            boolean isURL = (srcName.startsWith("http://") || srcName.startsWith("file://"));
            if (isURL) {
                if (optUpdateMsg != null) logMsg(optUpdateMsg);
                String sDots = "";
                URL url = new URL(srcName);
                InputStream urlIS = url.openStream();
                int nTotBytesRead = 0;
                while (true) {
                    if (optUpdateMsg != null) {
                        sDots += ".";
                        String sPct = (optEstInputFileLth > 0) ? ((int) ((100 * nTotBytesRead) / optEstInputFileLth)) + "% " : "", sProgress = "Copying " + sPct + sDots + "\n";
                        logMsg(sProgress);
                    }
                    nBytesRead = urlIS.read(buf);
                    nTotBytesRead += nBytesRead;
                    if (nBytesRead == -1) break; else {
                        dstFOS.write(buf, 0, nBytesRead);
                        nBytesWritten += nBytesRead;
                    }
                }
                dstFOS.close();
                if (optUpdateMsg != null) {
                    logMsg("\n");
                }
            } else {
                srcFIS = new FileInputStream(new File(srcName));
                while (true) {
                    nBytesRead = srcFIS.read(buf);
                    if (nBytesRead == -1) break; else {
                        dstFOS.write(buf, 0, nBytesRead);
                        nBytesWritten += nBytesRead;
                    }
                }
                srcFIS.close();
                dstFOS.close();
            }
        } catch (Exception e1) {
            return (false);
        }
        return (true);
    }
}
