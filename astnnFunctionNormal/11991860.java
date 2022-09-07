class BackupThread extends Thread {
    public static DicomObject loadDicomObject(URL url) {
        DicomInputStream dis;
        if (url == null) {
            TestMultiFrameImage.log.warn("No URL provided.");
            return null;
        }
        try {
            dis = new DicomInputStream(url.openStream());
            dis.setHandler(TestJaxbEncode.stopHandler);
            return (dis.readDicomObject());
        } catch (IOException e) {
            TestMultiFrameImage.log.warn("Unable to load object for " + url + " tests will not be run.");
            return null;
        }
    }
}
