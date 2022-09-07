class BackupThread extends Thread {
    protected BufferedReader getDataReader() {
        try {
            URL url = new URL(this.catalog.getCatalogURL());
            Debug.output("Catalog URL:" + url.toString());
            return new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException ex) {
            throw new SeismoException(ex);
        }
    }
}
