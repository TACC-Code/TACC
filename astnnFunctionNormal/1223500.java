class BackupThread extends Thread {
    private InputSource httpGet(String request, HttpClient client) {
        InputSource source = null;
        try {
            HttpGet httpGet = new HttpGet(request);
            HttpResponse httpResponse = client.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_BAD_REQUEST) {
                return null;
            }
            if (statusCode > HttpStatus.SC_BAD_REQUEST) {
                return null;
            }
            source = new InputSource(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source;
    }
}
