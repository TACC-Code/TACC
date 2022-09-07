class BackupThread extends Thread {
    public static String request(URL url) {
        InputStream is = null;
        String request = null;
        try {
            is = url.openStream();
            request = "";
            request = new Scanner(is).useDelimiter("\\Z").next();
        } catch (Exception e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return request;
    }
}
