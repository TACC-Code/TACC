class BackupThread extends Thread {
    public void login() {
        try {
            loginsuccessful = false;
            NULogger.getLogger().info("Getting startup cookie from uploaded.to");
            u = new URL("http://uploaded.to/");
            uc = (HttpURLConnection) u.openConnection();
            Map<String, List<String>> headerFields = uc.getHeaderFields();
            if (headerFields.containsKey("Set-Cookie")) {
                List<String> header = headerFields.get("Set-Cookie");
                for (int i = 0; i < header.size(); i++) {
                    tmp = header.get(i);
                    if (tmp.contains("PHPSESSID")) {
                        phpsessioncookie = tmp;
                    }
                }
            }
            phpsessioncookie = phpsessioncookie.substring(0, phpsessioncookie.indexOf(";"));
            NULogger.getLogger().log(Level.INFO, "phpsessioncookie: {0}", phpsessioncookie);
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to uploaded.to");
            HttpPost httppost = new HttpPost("http://uploaded.to/io/login");
            httppost.setHeader("Cookie", phpsessioncookie);
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("id", getUsername()));
            formparams.add(new BasicNameValuePair("pw", getPassword()));
            formparams.add(new BasicNameValuePair("_", ""));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            NULogger.getLogger().info("Getting cookies........");
            Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
            Cookie escookie = null;
            while (it.hasNext()) {
                escookie = it.next();
                if (escookie.getName().equalsIgnoreCase("login")) {
                    logincookie = "login=" + escookie.getValue();
                    NULogger.getLogger().info(logincookie);
                    loginsuccessful = true;
                    HostsPanel.getInstance().uploadedDotToCheckBox.setEnabled(true);
                }
                if (escookie.getName().equalsIgnoreCase("auth")) {
                    authcookie = "auth=" + escookie.getValue();
                    NULogger.getLogger().info(authcookie);
                }
            }
            if (loginsuccessful) {
                username = getUsername();
                password = getPassword();
                NULogger.getLogger().info("Uploaded.to Login success :)");
            } else {
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
                NULogger.getLogger().info("Uploaded.to Login failed :(");
            }
        } catch (Exception e) {
            NULogger.getLogger().log(Level.SEVERE, "{0}: Error in Uploaded.to Login", getClass().getName());
        }
    }
}
