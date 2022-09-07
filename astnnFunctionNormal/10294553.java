class BackupThread extends Thread {
    public static String getContentType(String urladr) {
        String out = Statics.S_NULL;
        try {
            URL url = new URL(urladr);
            URLConnection urlconnection = url.openConnection();
            out = urlconnection.getContentType();
        } catch (Exception e) {
        }
        return out;
    }
}
