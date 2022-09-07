class BackupThread extends Thread {
    private static synchronized String fetch(String addr) {
        try {
            StringBuffer buf = new StringBuffer(1024 * 40);
            URL url = new URL(addr);
            URLConnection con = url.openConnection();
            long length = con.getContentLength();
            long downloaded = 0;
            int read;
            char[] buffer = new char[1024];
            InputStream uin = con.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(uin));
            while ((read = in.read(buffer)) != -1) {
                downloaded += read;
                buf.append(buffer);
                if (bar != null) {
                    bar.setValue((int) (((double) downloaded / (double) length) * 100));
                }
            }
            if (bar != null) {
                bar.setValue(100);
            }
            return buf.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
