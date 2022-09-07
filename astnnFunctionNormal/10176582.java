class BackupThread extends Thread {
    public Component makeIntance() throws PluginException {
        try {
            Factory factory = new Factory(IoUtil.url2file(getContext()) + File.separator + getXmlFile(), IoUtil.url2file(getContext()) + File.separator + getXmlRulesFile());
            if (factory == null) throw new PluginException("No pudo crearse la factor�a para " + "levantar el componente.");
            Component component = (Component) factory.digest();
            if (component == null) throw new PluginException("No pudo cargarse el componente.");
            return component;
        } catch (IOException e) {
            throw new PluginException(e.getMessage());
        } catch (SAXException e) {
            throw new PluginException(e.getMessage());
        }
    }
}
