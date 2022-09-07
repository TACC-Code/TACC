class BackupThread extends Thread {
    private void systemConnect() throws ServiceNotAvailableException {
        mPairs.add(new BasicNameValuePair("method", "\"system.connect\""));
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(mPairs);
            String entityString = entity.toString();
            mSERVER.setEntity(entity);
            HttpResponse response = mClient.execute(mSERVER);
            InputStream result = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(result));
            JSONObject jso = new JSONObject(br.readLine());
            boolean error = jso.getBoolean("#error");
            String data = jso.getString("#data");
            if (error) {
                throw new ServiceNotAvailableException(data);
            }
            jso = new JSONObject(data);
            saveSession(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
