class BackupThread extends Thread {
    public void get(File localFile, String remote, int mode) {
        try {
            client.setFileType(mode);
            InputStream inRemote = client.retrieveFileStream(remote);
            OutputStream localOut = new FileOutputStream(localFile);
            byte[] buffer = new byte[FtpConstants.bufferSize];
            while (true) {
                int read = inRemote.read(buffer, 0, buffer.length);
                if (read == -1) break;
                localOut.write(buffer, 0, read);
            }
            localOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
