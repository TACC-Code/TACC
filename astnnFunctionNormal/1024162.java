class BackupThread extends Thread {
    public static void saveUrlToFile(final String url, final File f, final String proxyHost, final String proxyPort, final String nonProxyHosts, final int connectTimeout, final int readTimeout, final LoadingListener listener) throws IOException {
        final String method = "saveUrlToFile()";
        Trace.begin(CLASS, method);
        if (!isSetConnectionTimeOutSupported() && !IoUtility.isWebStarted()) {
            saveQedeqFromWebToBufferApache(url, f, proxyHost, proxyPort, nonProxyHosts, connectTimeout, readTimeout, listener);
            Trace.end(CLASS, method);
            return;
        }
        if (!IoUtility.isWebStarted()) {
            if (proxyHost != null) {
                System.setProperty("http.proxyHost", proxyHost);
            }
            if (proxyPort != null) {
                System.setProperty("http.proxyPort", proxyPort);
            }
            if (nonProxyHosts != null) {
                System.setProperty("http.nonProxyHosts", nonProxyHosts);
            }
        }
        FileOutputStream out = null;
        InputStream in = null;
        try {
            final URLConnection connection = new URL(url).openConnection();
            if (connection instanceof HttpURLConnection) {
                final HttpURLConnection httpConnection = (HttpURLConnection) connection;
                if (isSetConnectionTimeOutSupported()) {
                    try {
                        YodaUtility.executeMethod(httpConnection, "setConnectTimeout", new Class[] { Integer.TYPE }, new Object[] { new Integer(connectTimeout) });
                    } catch (NoSuchMethodException e) {
                        Trace.fatal(CLASS, method, "URLConnection.setConnectTimeout was previously found", e);
                    } catch (InvocationTargetException e) {
                        Trace.fatal(CLASS, method, "URLConnection.setConnectTimeout throwed an error", e);
                    }
                }
                if (isSetReadTimeoutSupported()) {
                    try {
                        YodaUtility.executeMethod(httpConnection, "setReadTimeout", new Class[] { Integer.TYPE }, new Object[] { new Integer(readTimeout) });
                    } catch (NoSuchMethodException e) {
                        Trace.fatal(CLASS, method, "URLConnection.setReadTimeout was previously found", e);
                    } catch (InvocationTargetException e) {
                        Trace.fatal(CLASS, method, "URLConnection.setReadTimeout throwed an error", e);
                    }
                }
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == 200) {
                    in = httpConnection.getInputStream();
                } else {
                    in = httpConnection.getErrorStream();
                    final String errorText = IoUtility.loadStreamWithoutException(in, 1000);
                    throw new IOException("Response code from HTTP server was " + responseCode + (errorText.length() > 0 ? "\nResponse  text from HTTP server was:\n" + errorText : ""));
                }
            } else {
                Trace.paramInfo(CLASS, method, "connection.getClass", connection.getClass().toString());
                in = connection.getInputStream();
            }
            if (!url.equals(connection.getURL().toString())) {
                throw new FileNotFoundException("\"" + url + "\" was substituted by " + "\"" + connection.getURL() + "\" from server");
            }
            final double maximum = connection.getContentLength();
            IoUtility.createNecessaryDirectories(f);
            out = new FileOutputStream(f);
            final byte[] buffer = new byte[4096];
            int bytesRead;
            int position = 0;
            while ((bytesRead = in.read(buffer)) != -1) {
                position += bytesRead;
                out.write(buffer, 0, bytesRead);
                if (maximum > 0) {
                    double completeness = position / maximum;
                    if (completeness < 0) {
                        completeness = 0;
                    }
                    if (completeness > 100) {
                        completeness = 1;
                    }
                    listener.loadingCompletenessChanged(completeness);
                }
            }
            listener.loadingCompletenessChanged(1);
        } finally {
            IoUtility.close(out);
            out = null;
            IoUtility.close(in);
            in = null;
            Trace.end(CLASS, method);
        }
    }
}
