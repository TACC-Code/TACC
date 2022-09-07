class BackupThread extends Thread {
    private String getServerData(String returnString) {
        InputStream is = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("nombre", nombre));
        nameValuePairs.add(new BasicNameValuePair("clave", clave));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(auth);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.d("Login", "Funciono http connection ");
        } catch (Exception e) {
            Log.e("Login", "Error en conexion http " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            String line = reader.readLine();
            is.close();
            result = line.trim();
            Log.d("Login", "Longitud line: " + line.trim().length());
        } catch (Exception e) {
            Log.e("Login", "Error convirtiendo resultado: " + e.toString());
        }
        Log.d("Login", "Funciono Json: " + result);
        Log.d("Login", "Longitud: " + result.trim().length());
        returnString += result;
        control = 99;
        if (result.trim().equals("1")) {
            control = 1;
        } else {
            control = 0;
        }
        Log.d("Login", "Control: " + control);
        return returnString;
    }
}
