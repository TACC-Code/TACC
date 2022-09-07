class BackupThread extends Thread {
    private void yourGames(DefaultHttpClient httpclient) throws IOException, ClientProtocolException {
        HttpGet httpget = new HttpGet(Constants.YOUR_GAMES_URL);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        System.out.println("yg form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        System.out.println("yg set of cookies:");
        List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
    }
}
