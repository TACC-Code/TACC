class BackupThread extends Thread {
        public byte[] getEmmaClassBytes(byte[] classBytes, String slashedName, long unitLastModified) {
            URL url = Thread.currentThread().getContextClassLoader().getResource(slashedName + ".class");
            if (url != null) {
                try {
                    URLConnection conn = url.openConnection();
                    if (conn.getLastModified() >= unitLastModified) {
                        byte[] result = Util.readURLConnectionAsBytes(conn);
                        if (result != null) {
                            return result;
                        }
                    }
                } catch (IOException ignored) {
                }
            }
            return classBytes;
        }
}
