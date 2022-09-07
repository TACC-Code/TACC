class BackupThread extends Thread {
    public static void copy(InputStream from, OutputStream to) throws IORuntimeException {
        byte buffer[] = new byte[4096];
        int read;
        try {
            while ((read = from.read(buffer)) != -1) to.write(buffer, 0, read);
            to.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
