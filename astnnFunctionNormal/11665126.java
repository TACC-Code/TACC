class BackupThread extends Thread {
    static void testIfModifiedSince() {
        try {
            URL available_url = new URL(OOO_DICTS + "available.lst");
            HttpURLConnection connect = (HttpURLConnection) available_url.openConnection();
            Calendar c = Calendar.getInstance();
            c.set(2008, 8, 1);
            connect.setIfModifiedSince(c.getTimeInMillis());
            connect.connect();
            System.out.println("return code is :" + connect.getResponseCode());
            System.out.println("content length is:" + connect.getContentLength());
            c.set(2001, 8, 1);
            connect = (HttpURLConnection) available_url.openConnection();
            connect.setIfModifiedSince(c.getTimeInMillis());
            connect.connect();
            System.out.println("return code is :" + connect.getResponseCode());
            System.out.println("content length is:" + connect.getContentLength());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
