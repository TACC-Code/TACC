class BackupThread extends Thread {
    public ByteArrayInputStream getFile(String fileName) throws IOException {
        File file = makeFile(fileName);
        if (logger.isDebugEnabled()) logger.debug("Getting file " + file);
        FileInputStream is = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        int read;
        while ((read = is.read(buffer)) > 0) baos.write(buffer, 0, read);
        is.close();
        return null;
    }
}
