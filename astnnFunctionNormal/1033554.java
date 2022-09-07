class BackupThread extends Thread {
    public synchronized byte[] digestAsByteArray(File file) throws Exception {
        digestAgent.reset();
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        for (int bytesRead = 0; (bytesRead = is.read(buffer)) >= 0; ) {
            digestAgent.update(buffer, 0, bytesRead);
        }
        is.close();
        byte[] digest = digestAgent.digest();
        return digest;
    }
}
