class BackupThread extends Thread {
    public static URLConnection openConnection(URL url, ParserFeedback feedback) throws ParserException {
        Map properties;
        String key;
        String value;
        URLConnection ret;
        try {
            ret = url.openConnection();
            properties = getDefaultRequestProperties();
            if (null != properties) for (Iterator iterator = properties.keySet().iterator(); iterator.hasNext(); ) {
                key = (String) iterator.next();
                value = (String) properties.get(key);
                ret.setRequestProperty(key, value);
            }
        } catch (IOException ioe) {
            String msg = "HTMLParser.openConnection() : Error in opening a connection to " + url.toExternalForm();
            ParserException ex = new ParserException(msg, ioe);
            if (null != feedback) feedback.error(msg, ex);
            throw ex;
        }
        return (ret);
    }
}
