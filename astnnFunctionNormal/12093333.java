class BackupThread extends Thread {
    public String call(String method, BasicNameValuePair[] parameters) throws ServiceNotAvailableException {
        String sessid = this.getSessionID();
        mPairs.clear();
        String nonce = Integer.toString(new Random().nextInt());
        Mac hmac;
        try {
            hmac = Mac.getInstance(JSONServerClient.mALGORITHM);
            final Long timestamp = new Date().getTime() / 100;
            final String time = timestamp.toString();
            hmac.init(new SecretKeySpec(JSONServerClient.mAPI_KEY.getBytes(), JSONServerClient.mALGORITHM));
            String message = time + ";" + JSONServerClient.mDOMAIN + ";" + nonce + ";" + method;
            hmac.update(message.getBytes());
            String hmac_value = new String(Hex.encodeHex(hmac.doFinal()));
            mPairs.add(new BasicNameValuePair("hash", "\"" + hmac_value + "\""));
            mPairs.add(new BasicNameValuePair("domain_name", "\"" + JSONServerClient.mDOMAIN + "\""));
            mPairs.add(new BasicNameValuePair("domain_time_stamp", "\"" + time + "\""));
            mPairs.add(new BasicNameValuePair("nonce", "\"" + nonce + "\""));
            mPairs.add(new BasicNameValuePair("method", "\"" + method + "\""));
            mPairs.add(new BasicNameValuePair("api_key", "\"" + JSONServerClient.mAPI_KEY + "\""));
            mPairs.add(new BasicNameValuePair("sessid", "\"" + sessid + "\""));
            for (int i = 0; i < parameters.length; i++) {
                mPairs.add(parameters[i]);
            }
            mSERVER.setEntity(new UrlEncodedFormEntity(mPairs));
            HttpResponse response = mClient.execute(mSERVER);
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String result = br.readLine();
            JSONObject jso;
            jso = new JSONObject(result);
            boolean error = jso.getBoolean("#error");
            if (error) {
                String errorMsg = jso.getString("#data");
                throw new ServiceNotAvailableException(errorMsg);
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ServiceNotAvailableException("Remote server is not available");
        }
        return null;
    }
}
