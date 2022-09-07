class BackupThread extends Thread {
    private void sendHttpRequest(String str) {
        final String uri = "http://voctrl.appspot.com/input/" + str;
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            BufferedReader bufedReader = null;
            HttpResponse httpResponse = client.execute(new HttpGet(uri));
            bufedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String line = bufedReader.readLine();
            if (Constants.SEND_OK.equals(line)) {
                Toast.makeText(this, "���M�Ȃ�", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
