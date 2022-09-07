class BackupThread extends Thread {
    public void doQuery(URL url, Object message, int firstRecord, String fileName) {
        String body = (String) message;
        FileOutputStream output = null;
        try {
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestProperty("SOAPAction", "post");
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            OutputStreamWriter w = new OutputStreamWriter(c.getOutputStream(), "UTF-8");
            w.write(body);
            w.flush();
            InputStream is = c.getInputStream();
            byte[] buf = new byte[1024];
            int len;
            String str = "";
            while ((len = is.read(buf)) > 0) {
                str = str + new String(buf, 0, len);
            }
            System.out.println(str);
            output = new FileOutputStream(new File(fileName));
            output.write(str.getBytes());
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
