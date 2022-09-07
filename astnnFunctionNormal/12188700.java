class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    private void loadUrl() throws IOException {
        HttpServletRequest request = Mailer.getRequest();
        try {
            if (parameters != null) {
                StringBuilder query = new StringBuilder();
                if (url.getQuery() != null) {
                    query.append('?');
                    query.append(url.getQuery());
                }
                FlashScope flash = null;
                if (request == null) log.debug("If you are trying to email a page from this server things will work better if you create the Mailer with an HttpServletRequest"); else {
                    flash = getFlashScope(request);
                    if (flash != null) query.append(query.length() == 0 ? '?' : '&').append(StripesConstants.URL_KEY_FLASH_SCOPE_ID).append('=').append(flash.key()); else log.warn("Couldn't create FlashScope!");
                }
                for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                    Object o = parameter.getValue();
                    if (o == null) continue;
                    Configuration configuration = StripesFilter.getConfiguration();
                    FormatterFactory factory = configuration.getFormatterFactory();
                    Class clazz = o.getClass();
                    Formatter formatter = factory.getFormatter(clazz, Locale.getDefault(), null, null);
                    String name = parameter.getKey();
                    String value = null;
                    if (formatter != null) value = formatter.format(o); else value = o.toString();
                    log.trace("Adding parameter ", name, " with value ", value);
                    query.append('&').append(URLEncoder.encode(name, "UTF-8")).append('=').append(URLEncoder.encode(value, "UTF-8"));
                    if (flash != null) flash.put(name, o);
                }
                releaseFlashScope(flash);
                url = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath() + query);
            }
            log.debug("Retrieving data from ", url);
            URLConnection connection = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) connection;
            if (request != null) {
                String cookieName = System.getProperty("org.apache.catalina.JSESSIONID", "JSESSIONID");
                HttpSession session = request.getSession(false);
                if (session != null) {
                    String cookieValue = session.getId();
                    http.addRequestProperty("Cookie", cookieName + "=" + cookieValue);
                }
            }
            contentType = http.getContentType();
            byte[] data = new byte[4096];
            int count;
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            StringBuilder content = new StringBuilder();
            while ((count = in.read(data)) != -1) content.append(new String(data, 0, count));
            in.close();
            this.content = content.toString();
        } catch (UnsupportedEncodingException e) {
            log.error(e, "This should NEVER happen!");
        } catch (MalformedURLException e) {
            log.error(e);
        }
    }
}
