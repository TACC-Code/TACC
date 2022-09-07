class BackupThread extends Thread {
    public void loginWuploader() throws Exception {
        login = false;
        u = new URL("http://api.wupload.com/utility?method=getWuploadDomainForCurrentIp");
        br = new BufferedReader(new InputStreamReader(u.openStream()));
        String k = "";
        while ((tmp = br.readLine()) != null) {
            k += tmp;
        }
        wudomain = CommonUploaderTasks.parseResponse(k, "\"response\":\"", "\"");
        NULogger.getLogger().log(Level.INFO, "WUpload Domain: {0}", wudomain);
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        NULogger.getLogger().info("Trying to log in to Wupload");
        HttpPost httppost = new HttpPost("http://www" + wudomain + "/account/login");
        httppost.setHeader("Referer", "http://www.wupload.com/");
        httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("email", getUsername()));
        formparams.add(new BasicNameValuePair("password", getPassword()));
        formparams.add(new BasicNameValuePair("redirect", "/"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        NULogger.getLogger().info("Getting wupload cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("PHPSESSID")) {
                sessioncookie = "PHPSESSID=" + escookie.getValue();
                NULogger.getLogger().info(sessioncookie);
            }
            if (escookie.getName().equalsIgnoreCase("email")) {
                mailcookie = "email=" + escookie.getValue();
                login = true;
                NULogger.getLogger().info(mailcookie);
            }
            if (escookie.getName().equalsIgnoreCase("nickname")) {
                namecookie = "nickname=" + escookie.getValue();
                NULogger.getLogger().info(namecookie);
            }
            if (escookie.getName().equalsIgnoreCase("isAffiliate")) {
                affiliatecookie = "isAffiliate=" + escookie.getValue();
                NULogger.getLogger().info(affiliatecookie);
            }
            if (escookie.getName().equalsIgnoreCase("role")) {
                rolecookie = "role=" + escookie.getValue();
                NULogger.getLogger().info(rolecookie);
            }
        }
        if (login) {
            NULogger.getLogger().info("Wupload Login Success");
            loginsuccessful = true;
            username = getUsername();
            password = getPassword();
            getFolderCookies();
        } else {
            NULogger.getLogger().info("Wupload Login failed");
            loginsuccessful = false;
            username = "";
            password = "";
            JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
            AccountsManager.getInstance().setVisible(true);
        }
    }
}
