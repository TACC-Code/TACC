class BackupThread extends Thread {
    public int login() {
        int returnValue = -1;
        try {
            get = new HttpGet(fidUrl + "/BasicWebsite/LogOnAndPresentation/U1.aspx");
            page = client.execute(get, responseHandler);
            in = new BufferedReader(new StringReader(page));
            tmp = "";
            while ((tmp = in.readLine()) != null) {
                if (tmp.contains("__VIEWSTATE")) {
                    viewState = tmp.substring(tmp.lastIndexOf("value=\"") + 7, tmp.lastIndexOf("\""));
                } else if (tmp.contains("__EVENTVALIDATION")) {
                    eventValidation = tmp.substring(tmp.lastIndexOf("value=\"") + 7, tmp.lastIndexOf("\""));
                }
            }
            HttpPost httpost = new HttpPost(fidUrl + "/BasicWebsite/LogOnAndPresentation/U1.aspx");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("__VIEWSTATE", viewState));
            nvps.add(new BasicNameValuePair("__EVENTVALIDATION", eventValidation));
            nvps.add(new BasicNameValuePair("logOnMain$UserName", username));
            nvps.add(new BasicNameValuePair("logOnMain$Password", password));
            nvps.add(new BasicNameValuePair("logOnMain$LoginButton", ""));
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = client.execute(httpost);
            if (response.getStatusLine().toString().equals("HTTP/1.1 302 Found")) returnValue = 0; else if (response.getStatusLine().toString().equals("HTTP/1.1 200 OK")) returnValue = 1; else returnValue = 2;
            EntityUtils.consume(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = -1;
        }
        return returnValue;
    }
}
