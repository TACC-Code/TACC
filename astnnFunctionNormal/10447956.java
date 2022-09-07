class BackupThread extends Thread {
    private boolean init() {
        if (connOpen) return true;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            String encoding = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
            urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            connOpen = true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
