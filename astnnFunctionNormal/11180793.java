class BackupThread extends Thread {
    private InputStream readMealsFromRSS() {
        URL url = null;
        InputStream is;
        try {
            url = new URL("http://songbook.me/services/mensa/");
        } catch (MalformedURLException e1) {
        }
        try {
            is = url.openStream();
        } catch (Exception e) {
            return null;
        }
        return is;
    }
}
