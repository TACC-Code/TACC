class BackupThread extends Thread {
    public static void postHttpCommand(ArrayList<NameValuePair> nameValues) throws Exception {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            HttpPost httppostObj = DBMediator.getHttpPost();
            httppostObj.setEntity(new UrlEncodedFormEntity(nameValues, "UTF-8"));
            DBMediator.setHttpResponse(DBMediator.getHttpClient().execute(httppostObj));
            inputStream = DBMediator.getHttpEntity(DBMediator.getHttpResponse()).getContent();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                Log.i("Response ", line);
                sb.append(line + "\n");
            }
            response = sb.toString();
            Log.i("RESULT OF DB OPERATION IS ", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        inputStream.close();
        inputStreamReader.close();
        bufferedReader.close();
    }
}
