class BackupThread extends Thread {
    Vector<String> getSetNames() {
        Vector<String> ret = new Vector<String>();
        Proxy p = null;
        if (mProxyType == 0) p = Proxy.NO_PROXY; else {
            try {
                p = new Proxy(PROXY_TYPES[mProxyType], new InetSocketAddress(jTextFieldProxyURL.getText(), Integer.parseInt(jTextFieldProxyPort.getText())));
            } catch (Exception e) {
                e.printStackTrace();
                Configuration.getConfiguration().getDebugEntity().addLog(e, Logable.LOG_ERROR);
                return ret;
            }
        }
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                String baseUrl = jTextFieldBaseUrl.getText();
                URL url = new URL(baseUrl);
                URLConnection conn = url.openConnection(p);
                conn.setDoOutput(true);
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                boolean save = false;
                while ((line = rd.readLine()) != null) {
                    if (line.indexOf("Filter Card Set") != -1) save = true;
                    if (line.indexOf("Filter Card Type") != -1) save = false;
                    if (save) {
                        if (line.indexOf("<option value=\"") != -1) {
                            String name = line.substring(line.indexOf("\"") + 1, line.indexOf("\">"));
                            name = csa.util.UtilityString.fromXML(name);
                            ret.addElement(name);
                        }
                    }
                }
                rd.close();
            } catch (Exception e) {
                e.printStackTrace();
                Configuration.getConfiguration().getDebugEntity().addLog(e, Logable.LOG_ERROR);
            }
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
        return ret;
    }
}
