class BackupThread extends Thread {
    public static URLConnection openConnection(String string, ParserFeedback feedback) throws ParserException {
        final String prefix = "file://localhost";
        String resource;
        URL url;
        StringBuffer buffer;
        URLConnection ret;
        try {
            url = new URL(fixSpaces(string));
            ret = openConnection(url, feedback);
        } catch (MalformedURLException murle) {
            try {
                File file = new File(string);
                resource = file.getCanonicalPath();
                buffer = new StringBuffer(prefix.length() + resource.length());
                buffer.append(prefix);
                if (!resource.startsWith("/")) buffer.append("/");
                buffer.append(resource);
                url = new URL(fixSpaces(buffer.toString()));
                ret = openConnection(url, feedback);
                if (null != feedback) feedback.info(url.toExternalForm());
            } catch (MalformedURLException murle2) {
                String msg = "HTMLParser.openConnection() : Error in opening a connection to " + string;
                ParserException ex = new ParserException(msg, murle2);
                if (null != feedback) feedback.error(msg, ex);
                throw ex;
            } catch (IOException ioe) {
                String msg = "HTMLParser.openConnection() : Error in opening a connection to " + string;
                ParserException ex = new ParserException(msg, ioe);
                if (null != feedback) feedback.error(msg, ex);
                throw ex;
            }
        }
        return (ret);
    }
}
