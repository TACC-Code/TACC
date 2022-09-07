class BackupThread extends Thread {
    private long updateQuota() {
        if (quota < NUMBER_FETCH_COUNT * 64) {
            try {
                HttpResponse response = client.execute(getQuota);
                InputStream stream = response.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(stream));
                try {
                    quota = Integer.valueOf(r.readLine());
                } finally {
                    r.close();
                }
            } catch (IOException iOException) {
                Logger.getLogger(HttpNumberFetcher.class.getName()).log(Level.WARNING, null, iOException);
                return quota = -1;
            } catch (IllegalStateException illegalStateException) {
                Logger.getLogger(HttpNumberFetcher.class.getName()).log(Level.WARNING, null, illegalStateException);
                return quota = -1;
            } catch (NumberFormatException numberFormatException) {
                Logger.getLogger(HttpNumberFetcher.class.getName()).log(Level.SEVERE, null, numberFormatException);
                return quota = -1;
            }
        }
        return quota;
    }
}
