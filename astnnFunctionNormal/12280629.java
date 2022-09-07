class BackupThread extends Thread {
    public static String getURL(String target) {
        StringBuffer page = new StringBuffer();
        try {
            URL url = new URL(target);
            InputStream in = url.openStream();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1; ) {
                page.append(new String(b, 0, n, "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page.toString();
    }
}
