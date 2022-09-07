class BackupThread extends Thread {
    public COLLADA load(URL url) throws IOException, JAXBException {
        return load(new BufferedInputStream(url.openStream()));
    }
}
