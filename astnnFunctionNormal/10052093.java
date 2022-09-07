class BackupThread extends Thread {
    int postData(String repository, String mime) {
        try {
            URL url = new URL(repository + "/cgi-bin/upload.cgi");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Close");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=-----------------------------18042893838469308861681692777");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(mime);
            os.flush();
            os.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.indexOf("<h2") > -1) {
                    int st = str.indexOf(">");
                    int en = str.indexOf("</h");
                    if (st < 0 || en < 0) {
                        Logger.reportError("Error", "Can not Understand Server Response: " + str);
                    } else {
                        if (str.indexOf("Success") > -1) {
                            Logger.reportError("Info", "Patch Uploaded Successfully");
                            return 1;
                        } else {
                            Logger.reportError("Error", str.substring(st + 1, en));
                        }
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            Logger.reportStatus(e);
        }
        return 0;
    }
}
