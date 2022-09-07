class BackupThread extends Thread {
    private ContainerService getContainer(URL url) {
        ConfigHandler ch = new ConfigHandler();
        InputStream is = null;
        try {
            is = url.openStream();
            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(ch);
            xr.parse(new InputSource(is));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) is.close();
            } catch (Throwable t) {
            }
        }
        return cfgnm_cntr_map.get(ch.configName);
    }
}
