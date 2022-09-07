class BackupThread extends Thread {
    public final void load(final URL url, final int timeout) {
        try {
            EncogLogging.log(EncogLogging.LEVEL_DEBUG, "Loading RSS from:" + url);
            final URLConnection http = url.openConnection();
            if (timeout > 0) {
                http.setConnectTimeout(timeout);
            }
            http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; " + "Win64; x64; Trident/4.0)");
            final InputStream is = http.getInputStream();
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final Document d = factory.newDocumentBuilder().parse(is);
            final Element e = d.getDocumentElement();
            final NodeList nl = e.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                final Node node = nl.item(i);
                final String nodename = node.getNodeName();
                if (nodename.equalsIgnoreCase("channel")) {
                    loadChannel(node);
                } else if (nodename.equalsIgnoreCase("item")) {
                    loadItem(node);
                }
            }
        } catch (final IOException e) {
            EncogLogging.log(e);
            throw new BotError(e);
        } catch (final SAXException e) {
            EncogLogging.log(e);
            throw new BotError(e);
        } catch (final ParserConfigurationException e) {
            EncogLogging.log(e);
            throw new BotError(e);
        }
    }
}
