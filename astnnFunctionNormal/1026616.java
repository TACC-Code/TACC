class BackupThread extends Thread {
    public void excutePOST(String cookies, Hashtable<String, String> parameters) {
        try {
            StringBuffer urlParameters = new StringBuffer();
            Enumeration<String> pit = parameters.keys();
            int paramCounter = 0;
            while (pit.hasMoreElements()) {
                String name = pit.nextElement();
                String value = parameters.get(name);
                if (paramCounter > 1) urlParameters.append('&');
                urlParameters.append(name).append(URLEncoder.encode(value, "UTF-8"));
                paramCounter++;
            }
            url = new URL(protocol, gameHost, request);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
            conn.setRequestProperty("Accept", "text/javascript, text/html, application/xml, text/xml");
            conn.setRequestProperty("Referer", "http://www.volvooceanracegame.org/home.php");
            conn.setRequestProperty("Cookie", cookies);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Writer post = new OutputStreamWriter(conn.getOutputStream());
            post.write(urlParameters.toString());
            post.write("\r\n");
            post.flush();
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
