class BackupThread extends Thread {
    public Scene load(URL url) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        fileType = FILE_TYPE_URL;
        setInternalBaseUrl(url);
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
        Scene returnScene = load(reader);
        fileType = FILE_TYPE_NONE;
        return returnScene;
    }
}
