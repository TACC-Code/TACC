class BackupThread extends Thread {
    private void genericPointTo(String target, String content, int method) {
        this.rawHTML = null;
        this.textCache = null;
        this.url = null;
        this.addressBarText = target;
        if (content != null && content.length() > 0) {
            addressBarText += ("?" + content);
        }
        HttpURLConnection urlConn;
        BufferedReader input;
        StringBuilder b = new StringBuilder();
        try {
            if (target == null) {
                throw new BrowserException("No URL given");
            }
            url = new URL(target);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("User-agent", USER_AGENT);
            String cookieStr = buildCookieStr(target);
            urlConn.setRequestProperty("Cookie", cookieStr);
            if (method == WebForm.POST) {
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConn.setRequestProperty("Content-Length", content.length() + "");
                if (!content.equals("")) {
                    DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
                    printout.writeBytes(content);
                    printout.flush();
                    printout.close();
                }
            } else if (method == WebForm.GET) {
                urlConn.setRequestMethod("GET");
            }
            input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String str;
            long timeout = 32;
            while (!input.ready()) {
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                }
                if (timeout > 5000) {
                    throw new BrowserException("input stream was not ready");
                }
                timeout *= 2;
            }
            while (null != ((str = input.readLine()))) {
                b.append(str).append('\n');
            }
            input.close();
            Map<String, List<String>> headers = urlConn.getHeaderFields();
            List<String> cookies = headers.get("Set-Cookie");
            if (cookies != null) {
                for (String cook : cookies) {
                    setCookie(cook, target);
                }
            }
        } catch (MalformedURLException e) {
            throw new BrowserException("Not a valid URL: " + target);
        } catch (ProtocolException e) {
            throw new BrowserException("Encountered a protocol exception: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BrowserException("Encountered a general Input/Output exception: " + e.getMessage());
        }
        this.rawHTML = b.toString();
    }
}
