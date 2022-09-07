class BackupThread extends Thread {
    public int read() {
        File fileTemp = new File(trustedLocationLocal);
        int fileLength = (int) fileTemp.length();
        byte[] fileBytes = new byte[fileLength];
        byte[] decryptedBytes = null;
        byte[] block = null;
        int result = 0;
        if (fileTemp.exists()) {
            try {
                RandomAccessFile raf = new RandomAccessFile(fileTemp, "rw");
                int length = raf.read(fileBytes);
                try {
                    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
                    block = cipher.doFinal(fileBytes);
                } catch (Exception e) {
                    this.showMessage("Using secretKey to read TCB failed", SAWSTextOutputCallback.ERROR);
                    if (debugLevel >= SAWSConstant.ErrorInfo) sawsDebugLog.write(e + "\nUsing secretKey to read TCB failed");
                    result = -1;
                }
                raf.close();
            } catch (Exception e) {
                if (debugLevel >= SAWSConstant.ErrorInfo) sawsDebugLog.write(e);
                result = -1;
            }
            if (result != -1) {
                result = extractASN1Block(block);
            }
        }
        return result;
    }
}
