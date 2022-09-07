class BackupThread extends Thread {
    public void removeID3V1Tag() throws Exception {
        SourceFile oTmpFileSource = null;
        InputStream oSourceIS = null;
        OutputStream oTmpOS = null;
        try {
            try {
                oSourceIS = new BufferedInputStream(sourceFile.getInputStream());
            } catch (Exception e) {
                throw new Exception("Error opening [" + sourceFile.getName() + "]", e);
            }
            try {
                try {
                    oTmpFileSource = sourceFile.createTempFile("id3.", ".tmp");
                } catch (Exception e) {
                    throw new Exception("Unable to create temporary file.", e);
                }
                try {
                    oTmpOS = new BufferedOutputStream(oTmpFileSource.getOutputStream());
                } catch (Exception e) {
                    throw new Exception("Error opening temporary file for writing.", e);
                }
                try {
                    long lFileLength = sourceFile.length();
                    byte[] abyBuffer = new byte[65536];
                    long lCopied = 0;
                    long lTotalToCopy = lFileLength - 128;
                    while (lCopied < lTotalToCopy) {
                        long lLeftToCopy = lTotalToCopy - lCopied;
                        long lToCopyNow = (lLeftToCopy >= 65536) ? 65536 : lLeftToCopy;
                        oSourceIS.read(abyBuffer, 0, (int) lToCopyNow);
                        oTmpOS.write(abyBuffer, 0, (int) lToCopyNow);
                        lCopied += lToCopyNow;
                    }
                    byte[] abyCheckTag = new byte[3];
                    oSourceIS.read(abyCheckTag);
                    if (!((abyCheckTag[0] == 'T') && (abyCheckTag[1] == 'A') && (abyCheckTag[2] == 'G'))) {
                        oTmpOS.write(abyCheckTag);
                        for (int i = 0; i < 125; i++) {
                            oTmpOS.write(oSourceIS.read());
                        }
                    }
                    oTmpOS.flush();
                } finally {
                    oTmpOS.close();
                }
            } finally {
                oSourceIS.close();
            }
            if (!sourceFile.delete()) {
                int iFails = 1;
                int iDelay = 1;
                while (!sourceFile.delete()) {
                    System.gc();
                    Thread.sleep(iDelay);
                    iFails++;
                    iDelay *= 2;
                    if (iFails > 10) {
                        throw new Exception("Unable to delete original file.");
                    }
                }
            }
            if (!oTmpFileSource.renameTo(sourceFile)) {
                throw new Exception("Unable to rename temporary file " + oTmpFileSource.toString() + " to " + sourceFile.toString() + ".");
            }
        } catch (Exception e) {
            throw new Exception("Error processing [" + sourceFile.getName() + "].", e);
        }
    }
}
