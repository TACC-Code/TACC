class BackupThread extends Thread {
    public static String getChecksum(File theFile, long start, long end) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(theFile, "r");
        byte[] buffer = new byte[KlangConstants.BUFFER_SIZE];
        MessageDigest complete = null;
        try {
            complete = MessageDigest.getInstance("MD5");
        } catch (Exception err) {
            err.printStackTrace();
            throw new IOException(KlangConstants.ERROR_GENERAL_FILE);
        }
        try {
            raf.seek(start);
            int numRead;
            do {
                KlangProgressWindow.progVal = KlangProgressWindow.PROG_MAX - (int) (((float) (end - raf.getFilePointer()) / (float) (end - start)) * KlangProgressWindow.PROG_MAX);
                if (raf.getFilePointer() >= (end - KlangConstants.BUFFER_SIZE)) {
                    buffer = new byte[(int) (end - raf.getFilePointer())];
                }
                numRead = raf.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead > 0);
            raf.close();
            byte[] inn = complete.digest();
            byte ch = 0x00;
            int i = 0;
            String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
            StringBuffer out = new StringBuffer(inn.length * 2);
            while (i < inn.length) {
                ch = (byte) (inn[i] & 0xF0);
                ch = (byte) (ch >>> 4);
                ch = (byte) (ch & 0x0F);
                out.append(pseudo[(int) ch]);
                ch = (byte) (inn[i] & 0x0F);
                out.append(pseudo[(int) ch]);
                i++;
            }
            return new String(out);
        } catch (IOException err) {
            raf.close();
            throw err;
        }
    }
}
