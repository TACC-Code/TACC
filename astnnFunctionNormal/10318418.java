class BackupThread extends Thread {
    private String getData(String myurl) throws Exception {
        URL url = new URL(myurl);
        uc = (HttpURLConnection) url.openConnection();
        if (enterUploadAccount.loginsuccessful) {
            uc.setRequestProperty("Cookie", enterUploadAccount.getLogincookie() + ";" + enterUploadAccount.getXfsscookie());
        }
        br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String temp = "", k = "";
        while ((temp = br.readLine()) != null) {
            k += temp;
        }
        br.close();
        return k;
    }
}
