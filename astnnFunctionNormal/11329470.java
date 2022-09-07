class BackupThread extends Thread {
    public static List<String> get(final String pUrl) throws IOException {
        try {
            Thread.sleep(2300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final List<String> result = new ArrayList<String>();
        BufferedReader input = null;
        URL url = new URL(pUrl);
        URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(false);
        urlConn.setUseCaches(false);
        urlConn.setConnectTimeout(1000);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String str = "";
        while (null != ((str = input.readLine()))) {
            result.add(str);
        }
        input.close();
        return result;
    }
}
