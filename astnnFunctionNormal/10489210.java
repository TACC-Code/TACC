class BackupThread extends Thread {
    private static Reader getReader(URL url) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }
}
