class BackupThread extends Thread {
    public static byte[] readAll(InputStream _in) {
        byte[] r = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (_in.available() > 0) out.write(_in.read());
            r = out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(FooLib.getClassName(ex.getClass()) + ":" + ex.getMessage());
        }
        return r;
    }
}
