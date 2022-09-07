class BackupThread extends Thread {
    public WcsAdapter createWcsAdapter(String urlString, DapAuthenticator auth) {
        try {
            String version = null;
            Authenticator.setDefault(auth);
            URL url = new URL(urlString + "?request=GetCapabilities&version=1.0.0&service=WCS");
            InputStream stream = url.openStream();
            XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(stream);
            while (eventReader.hasNext() && version == null) {
                XMLEvent event = (XMLEvent) eventReader.next();
                if (event instanceof StartElement && (((StartElement) event).getName().getLocalPart().equals("WCS_Capabilities"))) {
                    Iterator<Attribute> iter = ((StartElement) event).getAttributes();
                    while (iter.hasNext()) {
                        version = iter.next().getValue();
                    }
                }
            }
            if (version == null) {
                return null;
            } else if (version.equals("1.0.0")) {
                return new WcsAdapter_100(eventReader);
            } else if (version.equals("1.1.1")) {
                return null;
            }
        } catch (MalformedURLException e) {
            lastError = "Error connecting to server address: " + urlString + " exception message: " + e.toString();
            e.printStackTrace(System.out);
        } catch (IOException e) {
            lastError = "Error opening connection to server address:" + urlString + " exception message: " + e.toString();
            e.printStackTrace(System.out);
        } catch (TransformerFactoryConfigurationError e) {
            lastError = "Error with transformer factory configuration.  Exception message: " + e.toString();
            e.printStackTrace();
        } catch (XMLStreamException e) {
            lastError = "Error with XML stream.  Exception message: " + e.toString();
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            lastError = "Error with factory.  Exception message: " + e.toString();
            e.printStackTrace();
        }
        return null;
    }
}
