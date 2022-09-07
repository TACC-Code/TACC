class BackupThread extends Thread {
    public static RAFFile get(final URLConnection connection) throws IOException {
        RAFFile topBack = null;
        debug("get on " + connection);
        final Permission permission = connection.getPermission();
        int dictLength = connection.getContentLength();
        try {
            topBack = (RAFFile) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                RAFFile back = null;

                public Object run() throws IOException {
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        File tmpFile = File.createTempFile("dict_cache", null);
                        tmpFile.deleteOnExit();
                        if (tmpFile != null) {
                            in = connection.getInputStream();
                            out = new FileOutputStream(tmpFile);
                            int read = 0;
                            byte[] buf = new byte[BUF_SIZE];
                            while ((read = in.read(buf)) != -1) {
                                out.write(buf, 0, read);
                            }
                            back = new TemporaryRAFFile(tmpFile, permission);
                        } else {
                            back = new MemoryRAFFile(connection);
                        }
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    }
                    return back;
                }
            });
        } catch (PrivilegedActionException pae) {
            topBack = new MemoryRAFFile(connection);
        } catch (SecurityException se) {
            topBack = new MemoryRAFFile(connection);
        }
        return topBack;
    }
}
