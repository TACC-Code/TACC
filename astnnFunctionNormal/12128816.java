class BackupThread extends Thread {
    public static String openUrl(String url, String method, Bundle params) throws MalformedURLException, IOException {
        String strBoundary = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
        String endLine = "\r\n";
        OutputStream os;
        if (method.equals("GET")) {
            url = url + "?" + encodeUrl(params);
        }
        Log.d("Facebook-Util", method + " URL: " + url);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent") + " FacebookAndroidSDK");
        if (!method.equals("GET")) {
            Bundle dataparams = new Bundle();
            for (String key : params.keySet()) {
                if (params.getByteArray(key) != null) {
                    dataparams.putByteArray(key, params.getByteArray(key));
                }
            }
            if (!params.containsKey("method")) {
                params.putString("method", method);
            }
            if (params.containsKey("access_token")) {
                String decoded_token = URLDecoder.decode(params.getString("access_token"));
                params.putString("access_token", decoded_token);
            }
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + strBoundary);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(("--" + strBoundary + endLine).getBytes());
            os.write((encodePostBody(params, strBoundary)).getBytes());
            os.write((endLine + "--" + strBoundary + endLine).getBytes());
            if (!dataparams.isEmpty()) {
                for (String key : dataparams.keySet()) {
                    os.write(("Content-Disposition: form-data; filename=\"" + key + "\"" + endLine).getBytes());
                    os.write(("Content-Type: content/unknown" + endLine + endLine).getBytes());
                    os.write(dataparams.getByteArray(key));
                    os.write((endLine + "--" + strBoundary + endLine).getBytes());
                }
            }
            os.flush();
        }
        String response = "";
        try {
            response = read(conn.getInputStream());
        } catch (FileNotFoundException e) {
            response = read(conn.getErrorStream());
        }
        return response;
    }
}
