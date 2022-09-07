class BackupThread extends Thread {
    public static void loginGrUpload() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to grupload.com");
        HttpPost httppost = new HttpPost("http://grupload.com/");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("op", "login"));
        formparams.add(new BasicNameValuePair("redirect", "http://grupload.com"));
        formparams.add(new BasicNameValuePair("login", "007007dinesh"));
        formparams.add(new BasicNameValuePair("password", "************************"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("login")) {
                logincookie = "login=" + escookie.getValue();
                System.out.println(logincookie);
            }
            if (escookie.getName().equalsIgnoreCase("xfss")) {
                xfsscookie = "xfss=" + escookie.getValue();
                System.out.println(xfsscookie);
                login = true;
            }
        }
        if (login) {
            System.out.println("Grupload Login successful :)");
        } else {
            System.out.println("Grupload Login failed :(");
        }
    }
}
