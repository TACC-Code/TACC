class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        String urlStr = DEFAULT_URL;
        String connAs = CONNECT_AS;
        String streamTo = STREAM_TO;
        if (args.length > 0) {
            urlStr = args[0];
        }
        if (System.console() != null) {
            System.out.print("URL [" + urlStr + "]: ");
            String readStr = System.console().readLine();
            if ((readStr == null) || (readStr.length() <= 0)) {
                urlStr = readStr;
            }
            System.out.println();
        }
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(false);
        if (connAs.equals("wmp")) {
            System.out.println("Connecting as Windows Media Player");
            conn.setRequestProperty("user-agent", "NSPlayer/10.0.0.3702 WMFSDK/10.0");
            conn.setRequestProperty("accept", "*/*");
            conn.addRequestProperty("ua-cpu", "x86");
            conn.addRequestProperty("accept-encoding", "gzip, deflate");
        } else if (connAs.equals("winamp")) {
            System.out.println("Connecting as Winamp");
            conn.setRequestProperty("user-agent", "WinampMPEG/5.33");
            conn.setRequestProperty("accept", "*/*");
            conn.addRequestProperty("icy-metadata", "1");
            conn.addRequestProperty("connection", "close");
        } else if (connAs.equals("firefox")) {
            System.out.println("Connecting as Firefox");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; " + "rv:1.8.1.2) Gecko/20070219 Firefox/2.0.0.2");
            conn.setRequestProperty("accept", "text/xml,application/xml,application/xhtml+xml," + "text/html;q=0.9,text/plain;q=0.8,image/png,*/*;" + "q=0.5");
            conn.addRequestProperty("accept-language", "en-us,en;q=0.5");
            conn.addRequestProperty("accept-encoding", "gzip,deflate");
            conn.addRequestProperty("keep-alive", "300");
            conn.addRequestProperty("connection", "keep-alive");
        } else {
            System.out.println("Connecting as Java");
        }
        System.out.println("Connecting");
        conn.connect();
        System.out.println("Response headers");
        int num = 0;
        String field;
        while ((field = conn.getHeaderField(num)) != null) {
            System.out.println("[" + num + "] " + conn.getHeaderFieldKey(num) + " - " + field);
            num++;
        }
        System.out.println("Grabbing stream to file: " + streamTo);
        FileOutputStream out = new FileOutputStream(streamTo);
        int read = 0;
        byte[] buf = new byte[READ_BYTES];
        while ((read = conn.getInputStream().read(buf)) != -1) {
            out.write(buf, 0, read);
        }
        System.out.println("Done with message: " + conn.getResponseMessage());
        conn.disconnect();
    }
}
