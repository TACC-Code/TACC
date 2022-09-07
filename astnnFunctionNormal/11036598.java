class BackupThread extends Thread {
    public void run() {
        while (true) {
            StringBuffer strData = new StringBuffer();
            try {
                urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            IQuote quote = fetchQuote(strData);
        }
    }
}
