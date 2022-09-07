class BackupThread extends Thread {
    public ArrayList<T> parseDocument() throws ConsolewarsAPIException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        URL url;
        try {
            url = new URL(APIURL);
            URLConnection connection = url.openConnection();
            connection.connect();
            SAXParser parser = spf.newSAXParser();
            parser.parse(connection.getInputStream(), this);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (MalformedURLException e1) {
            throw new ConsolewarsAPIException("Es sind Verbindungsprobleme aufgetreten", e1);
        } catch (IOException e) {
            throw new ConsolewarsAPIException("Ein-/Ausgabefehler", e);
        }
        return items;
    }
}
