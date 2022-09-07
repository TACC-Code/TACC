class BackupThread extends Thread {
    public void run() {
        l.lock();
        try {
            updateQuota();
            if (this.quota < 0) {
                provider.setQuota(quota);
                return;
            }
            getNumbers.setURI(new URI(String.format(GET_NUMBERS_FORMAT_STRING, NUMBER_FETCH_COUNT * 2, provider.getSeed() < 0 ? "new" : provider.getSeed())));
            HttpResponse response = client.execute(getNumbers);
            InputStream stream = response.getEntity().getContent();
            LinkedList<Integer> list = new LinkedList<Integer>();
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            try {
                while (r.ready()) {
                    list.add((int) Long.parseLong(r.readLine().replaceAll("\t", ""), 16));
                }
            } finally {
                r.close();
            }
            provider.addNumbers(list);
            updateQuota();
            this.quota -= NUMBER_FETCH_COUNT * 32;
            System.out.println("quota: " + quota);
        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage() + " - " + ex.getReason());
            Logger.getLogger(HttpNumberFetcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(HttpNumberFetcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(HttpNumberFetcher.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            l.unlock();
        }
    }
}
