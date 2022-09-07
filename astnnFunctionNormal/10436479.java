class BackupThread extends Thread {
    public PrecomputedAnimatedModel loadPrecomputedModel(URL url, float framesPerSecond) throws IncorrectFormatException, ParsingErrorException, IOException {
        boolean baseURLWasNull = setBaseURLFromModelURL(url);
        PrecomputedAnimatedModel model = loadPrecomputedModel(new InputStreamReader(url.openStream()), url.toExternalForm(), framesPerSecond);
        if (baseURLWasNull) {
            popBaseURL();
        }
        return (model);
    }
}
