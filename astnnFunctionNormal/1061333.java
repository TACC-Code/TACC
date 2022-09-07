class BackupThread extends Thread {
    public String submit(String urlString) {
        String result = null;
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            result = conn.getContent().toString();
        } catch (Exception e) {
            result = e.toString();
        }
        return result;
    }
}
