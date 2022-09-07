class BackupThread extends Thread {
    @Deprecated
    public void submitTo(String target, HashMap<String, String> values) {
        Iterator<String> iter = values.keySet().iterator();
        String content = "";
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                content = content + "&" + key + "=" + URLEncoder.encode(values.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new BrowserException("Could not encode string: " + values.get(key));
            }
        }
        if (content.length() > 0) {
            content = content.substring(1);
        }
        this.rawHTML = null;
        this.textCache = null;
        this.url = null;
        HttpURLConnection urlConn;
        BufferedReader input;
        String rtn = "";
        try {
            if (target == null) {
                throw new BrowserException("No URL entered to point to");
            }
            url = new URL(target);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("User-agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.setRequestProperty("Content-Length", content.length() + "");
            if (!content.equals("")) {
                DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
                printout.writeBytes(content);
                printout.flush();
                printout.close();
            }
            input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String str;
            if (!input.ready()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                if (!input.ready()) {
                    throw new BrowserException("input stream was not ready");
                }
            }
            while (null != ((str = input.readLine()))) {
                rtn = rtn + str + "\n";
            }
            input.close();
        } catch (MalformedURLException e) {
            throw new BrowserException("Not a valid URL: " + target);
        } catch (ProtocolException e) {
            throw new BrowserException("Encountered a protocol exception: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BrowserException("Encountered a general Input/Output exception: " + e.getMessage());
        }
        this.rawHTML = rtn;
    }
}
