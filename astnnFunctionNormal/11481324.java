class BackupThread extends Thread {
    public final synchronized MibewResponse request(String suburl, String urlParameters) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(fUrl + suburl).openConnection();
            connection.setConnectTimeout(REQUEST_TIMEOUT);
            connection.setReadTimeout(REQUEST_TIMEOUT);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            if (fCookies.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Entry<String, String> cookie : fCookies.entrySet()) {
                    if (sb.length() > 0) {
                        sb.append("; ");
                    }
                    sb.append(cookie.getKey() + "=" + cookie.getValue());
                }
                connection.addRequestProperty("Cookie", sb.toString());
            }
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            InputStream is = connection.getInputStream();
            int len = connection.getContentLength();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(len < 256 ? 256 : len);
            byte b[] = new byte[1024];
            int size = 0;
            while ((size = is.read(b)) >= 0) {
                buffer.write(b, 0, size);
            }
            is.close();
            List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
            if (cookies != null) {
                for (String cookie : cookies) {
                    Matcher matcher = COOKIESET.matcher(cookie);
                    if (matcher.find()) {
                        String name = matcher.group(1);
                        String value = matcher.group(2);
                        fCookies.put(name, value);
                    }
                }
            }
            return new MibewResponse(connection.getResponseCode(), buffer.toByteArray());
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            throw new IOException("cannot connect: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
