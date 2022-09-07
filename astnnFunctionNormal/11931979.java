class BackupThread extends Thread {
    public static Document readXml(URL url) throws UnknownRtException {
        try {
            return readXml(url.openStream(), url.toExternalForm());
        } catch (java.net.MalformedURLException mue) {
            throw new UnknownRtException("Can't read XML file: " + url.toString(), mue);
        } catch (java.io.IOException ioe) {
            throw new UnknownRtException("Can't read XML file: " + url.toString(), ioe);
        } catch (Exception pce) {
            throw new UnknownRtException("Error parsing XML file: " + url.toString(), pce);
        }
    }
}
