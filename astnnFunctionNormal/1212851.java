class BackupThread extends Thread {
    public long setResourceContent(CalDAVTransaction transaction, String uri, InputStream is, String contentType, String characterEncoding) throws CalDAVException {
        File file = new File(this._root, uri);
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file), BUF_SIZE);
            try {
                int read;
                byte[] copyBuffer = new byte[BUF_SIZE];
                while ((read = is.read(copyBuffer, 0, copyBuffer.length)) != -1) {
                    os.write(copyBuffer, 0, read);
                }
            } finally {
                try {
                    is.close();
                } finally {
                    os.close();
                }
            }
        } catch (IOException _ex) {
            throw new CalDAVException(_ex);
        }
        long length = -1;
        try {
            length = file.length();
        } catch (SecurityException _ex) {
        }
        return length;
    }
}
