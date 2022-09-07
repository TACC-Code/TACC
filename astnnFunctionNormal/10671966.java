class BackupThread extends Thread {
    public static String getPage(URL url) {
        StringBuffer buffer = new StringBuffer();
        byte[] buf = new byte[1024];
        int amount = 0;
        try {
            InputStream input = url.openStream();
            while ((amount = input.read(buf)) > 0) {
                buffer.append(new String(buf, 0, amount));
            }
            input.close();
        } catch (Exception ex) {
            Tools.logException(Tools.class, ex, url.getPath());
            return null;
        }
        return buffer.toString();
    }
}
