class BackupThread extends Thread {
    public static TagLibrary create(URL url) throws IOException {
        InputStream is = null;
        TagLibrary t = null;
        URLConnection conn = null;
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            boolean schemaValidating = false;
            if (MyfacesConfig.getCurrentInstance(externalContext).isValidateXML()) {
                String version = ConfigFilesXmlValidationUtils.getFaceletTagLibVersion(url);
                if (schemaValidating = "2.0".equals(version)) {
                    ConfigFilesXmlValidationUtils.validateFaceletTagLibFile(url, externalContext, version);
                }
            }
            LibraryHandler handler = new LibraryHandler(url);
            SAXParser parser = createSAXParser(handler, externalContext, schemaValidating);
            conn = url.openConnection();
            conn.setUseCaches(false);
            is = conn.getInputStream();
            parser.parse(is, handler);
            t = handler.getLibrary();
        } catch (SAXException e) {
            IOException ioe = new IOException("Error parsing [" + url + "]: ");
            ioe.initCause(e);
            throw ioe;
        } catch (ParserConfigurationException e) {
            IOException ioe = new IOException("Error parsing [" + url + "]: ");
            ioe.initCause(e);
            throw ioe;
        } finally {
            if (is != null) is.close();
        }
        return t;
    }
}
