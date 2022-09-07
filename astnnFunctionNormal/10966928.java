class BackupThread extends Thread {
    public static String fetchDataFromURL(String urlstr, String postdata, boolean retrieveData) throws WrongPasswordException, IOException {
        URL url = null;
        URLConnection urlConn;
        DataOutputStream printout;
        String data = "";
        boolean wrong_pass = false;
        Debug.debug("Urlstr: " + urlstr);
        Debug.debug("Postdata: " + postdata);
        try {
            url = new URL(urlstr);
        } catch (MalformedURLException e) {
            Debug.error("URL invalid: " + urlstr);
            throw new MalformedURLException("URL invalid: " + urlstr);
        }
        if (url != null) {
            urlConn = url.openConnection();
            urlConn.setConnectTimeout(5000);
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            if (postdata != null) {
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                try {
                    printout = new DataOutputStream(urlConn.getOutputStream());
                    printout.writeBytes(postdata);
                    printout.flush();
                    printout.close();
                } catch (SocketTimeoutException ste) {
                    Debug.error("Could not fetch data from url: " + ste.toString());
                }
            }
            BufferedReader d;
            try {
                d = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String str;
                while (null != ((str = HTMLUtil.stripEntities(d.readLine())))) {
                    if ((str.indexOf("Das angegebene Kennwort ist ung�ltig") > 0) || (str.indexOf("Password not valid") > 0)) {
                        wrong_pass = true;
                    }
                    if (retrieveData) {
                        data += str;
                    }
                }
                d.close();
            } catch (IOException e1) {
                throw new IOException("Network unavailable");
            }
            if (wrong_pass) {
                int wait = 3;
                Pattern waitSeconds = Pattern.compile(PATTERN_WAIT_FOR_X_SECONDS);
                Matcher m = waitSeconds.matcher(data);
                if (m.find()) {
                    try {
                        wait = Integer.parseInt(m.group(1));
                    } catch (NumberFormatException nfe) {
                        wait = 3;
                    }
                }
                throw new WrongPasswordException("Password invalid", wait);
            }
        }
        return data;
    }
}
