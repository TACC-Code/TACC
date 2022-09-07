class BackupThread extends Thread {
    public Tree loadTree(URL url) throws IOException {
        return loadTree(url.openStream());
    }
}
