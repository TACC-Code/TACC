class BackupThread extends Thread {
    @Override
    public Cal3dScene loadScene(URL url) throws IOException, IncorrectFormatException, ParsingErrorException {
        boolean baseURLWasNull = setBaseURLFromModelURL(url);
        Cal3dScene scene = new Cal3dScene();
        loadCal3dModel(getBaseURL(), url.toExternalForm(), new InputStreamReader(url.openStream()), scene);
        if (baseURLWasNull) {
            popBaseURL();
        }
        return (scene);
    }
}
