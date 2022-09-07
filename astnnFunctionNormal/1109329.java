class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        loginGrUpload();
        initialize();
        fileUpload();
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        HttpPost httppost = new HttpPost("http://grupload.com/");
        if (login) {
            httppost.setHeader("Cookie", logincookie + ";" + xfsscookie);
        }
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("op", "upload_result"));
        formparams.add(new BasicNameValuePair("fn", fnvalue));
        formparams.add(new BasicNameValuePair("st", "OK"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        uploadresponse = EntityUtils.toString(httpresponse.getEntity());
        downloadlink = parseResponse(uploadresponse, "Download Link", "</textarea>");
        downloadlink = downloadlink.substring(downloadlink.indexOf("http://"));
        System.out.println("Download Link : " + downloadlink);
        deletelink = parseResponse(uploadresponse, "Delete Link", "</textarea>");
        deletelink = deletelink.substring(deletelink.indexOf("http://"));
        System.out.println("Delete Link : " + deletelink);
    }
}
