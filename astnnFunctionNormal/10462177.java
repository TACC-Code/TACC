class BackupThread extends Thread {
        private void processLocationUpdated(Location location) {
            String la = String.valueOf(location.getLatitude());
            String lo = String.valueOf(location.getLongitude());
            HttpPost req = new HttpPost(serverURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userId", userId));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("deviceSN", deviceSN.toString()));
            params.add(new BasicNameValuePair("latitude", la));
            params.add(new BasicNameValuePair("longtitude", lo));
            try {
                req.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = new DefaultHttpClient().execute(req);
                System.err.println(httpResponse.toString());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
