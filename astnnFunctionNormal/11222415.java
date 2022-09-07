class BackupThread extends Thread {
    public static Object[] fetch(String urlStr, String hashCode, String root, String prepend, String encoding) throws IOException {
        if (urlStr == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        Object[] fetchObj = new Object[2];
        Integer responseCode = new Integer(0);
        File file = new File(root + File.separator + prepend + hashCode);
        try {
            URL url = null;
            if (urlStr.indexOf(":", 5) > 5 && urlStr.indexOf("@") > 4) {
                String tmpUrl = "http://" + urlStr.substring(urlStr.indexOf("@") + 1);
                String uname = urlStr.substring(7, urlStr.indexOf(":", 7));
                String pass = urlStr.substring(urlStr.indexOf(":", 7) + 1, urlStr.indexOf("@"));
                java.net.Authenticator.setDefault(new MilspecAuthenticator(uname, pass));
                url = new URL(tmpUrl);
            } else {
                url = new URL(urlStr);
            }
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            responseCode = new Integer(urlConn.getResponseCode());
            String line = "";
            InputStreamReader isreader = new InputStreamReader(urlConn.getInputStream(), "UTF8");
            BufferedReader reader = new BufferedReader(isreader);
            OutputStreamWriter owriter = new OutputStreamWriter(new FileOutputStream(file, false), "UTF8");
            BufferedWriter writer = new BufferedWriter(owriter);
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("<!DOCTYPE") || line.startsWith("<!doctype")) {
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
            try {
                reader.close();
            } catch (Exception ioe) {
            }
            try {
                writer.close();
            } catch (Exception ioe) {
            }
        } catch (IOException ie) {
            throw new IOException("" + responseCode.intValue() + ":" + ie.getMessage());
        } catch (Exception e) {
            Flog.warn(classStr(), "Could not complete fetch. " + e.getMessage());
        }
        fetchObj[0] = responseCode;
        fetchObj[1] = file;
        System.out.println(urlStr + " returned " + file.getAbsolutePath());
        return fetchObj;
    }
}
