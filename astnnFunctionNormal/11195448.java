class BackupThread extends Thread {
    protected byte[] createHashFromFile(RandomAccessFile file, long length) {
        try {
            byte[] buffer = new byte[1024];
            MD4 md4 = new MD4();
            for (long chunks = length / buffer.length; chunks > 0; chunks--) {
                file.readFully(buffer);
                md4.update(buffer, 0, buffer.length);
            }
            file.readFully(buffer, 0, ((int) length % buffer.length));
            md4.update(buffer, 0, ((int) length % buffer.length));
            return md4.digest();
        } catch (IOException ioe) {
            return null;
        }
    }
}
