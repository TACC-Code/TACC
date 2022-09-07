class BackupThread extends Thread {
    protected void request() throws Exception {
        cal.clear();
        contract = getCurrentContract();
        URL url = new URL(contract.getUrlString());
        setInputStream(url.openStream());
    }
}
