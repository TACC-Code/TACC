class BackupThread extends Thread {
    public SBNURLLoader(AbstractRepository repo, Path path, URL url) throws IOException {
        super(repo.backend, path, url.openConnection().getInputStream(), null);
        this.url = url;
    }
}
