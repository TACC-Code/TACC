class BackupThread extends Thread {
    private void setBlobValue(OutputStream src, java.sql.Blob blob) throws Exception {
        InputStream stream = blob.getBinaryStream();
        byte[] b = new byte[4096];
        for (int len = -1; (len = stream.read(b, 0, 4096)) != -1; ) src.write(b, 0, len);
        src.flush();
        src.close();
        stream.close();
    }
}
