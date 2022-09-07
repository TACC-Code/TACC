class BackupThread extends Thread {
    public void putFile(GPESecurityManager secMgr, InputStream is, String remoteFile) throws Exception {
        LocalFileTransferClient transfer = (LocalFileTransferClient) storage.importFile(remoteFile, "Local", false);
        OutputStream os = transfer.getOutputStream();
        int chunk = 16384;
        byte[] buf = new byte[chunk];
        int read = 0;
        while (read != -1) {
            read = is.read(buf, 0, chunk);
            if (read != -1) {
                os.write(buf, 0, read);
            }
        }
        os.close();
        transfer.destroy();
    }
}
