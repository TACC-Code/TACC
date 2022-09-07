class BackupThread extends Thread {
    public static String request(URL url) {
        try {
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            InputStream downloadStream = request.getInputStream();
            byte[] buffer = new byte[1000 * 1000];
            byte[] smallbuff;
            smallbuff = new byte[downloadStream.available()];
            int k = 0;
            while (downloadStream.available() != 0) {
                downloadStream.read(smallbuff);
                for (int i = 0; i < smallbuff.length; i++) {
                    buffer[k] = smallbuff[i];
                    k++;
                }
                smallbuff = new byte[downloadStream.available()];
                Thread.sleep(40);
            }
            String tmp = new String(buffer);
            return tmp;
        } catch (Exception e) {
            return "RemoteINTERFACE: " + e;
        }
    }
}
