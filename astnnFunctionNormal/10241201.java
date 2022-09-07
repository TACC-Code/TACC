class BackupThread extends Thread {
    @SuppressWarnings("static-access")
    public static String getUrlString(String urlPath, String encoding) {
        String charSet = encoding.trim();
        BufferedReader in = null;
        String content = "";
        try {
            URL url = new URL(urlPath);
            URLConnection con = url.openConnection();
            con.setAllowUserInteraction(false);
            con.connect();
            String type = con.guessContentTypeFromStream(con.getInputStream());
            if (type == null) type = con.getContentType();
            if (type == null || type.trim().length() == 0 || (type.trim().indexOf("text/html") < 0 && type.trim().indexOf("text/xml") < 0)) {
            } else {
                if (type.indexOf("charset=") > 0) charSet = type.substring(type.indexOf("charset=") + 8);
            }
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), charSet));
            String temp;
            while ((temp = in.readLine()) != null) {
                content += temp + "\n";
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
            }
        }
    }
}
