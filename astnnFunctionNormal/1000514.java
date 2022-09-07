class BackupThread extends Thread {
    public WebGridBean(String descriptor) throws GridBeanException {
        this.descriptor = descriptor;
        try {
            URL url = new URL(WebClientSettings.getGridbeanPath() + descriptor + "/" + WebGridBeanConstants.GRIDBEAN_DESCRIPTION);
            InputStream gbdInput = new BufferedInputStream(url.openStream());
            Element root = new XercesDOMAdapter().getDocument(gbdInput, false).getDocumentElement();
            String appName = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.APPLICATION_NAME, null);
            String appVersion = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.APPLICATION_VERSION, null);
            application = new ApplicationImpl(appName, appVersion);
            name = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.NAME, null);
            version = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.VERSION, null);
            inputJspFile = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.INPUT_GUI, null);
            outputJspFile = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.OUTPUT_GUI, null);
            String tmp = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.ICON_FILE, null);
            if (tmp != null) icon = tmp;
            tmp = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.AUTHOR, null);
            if (tmp != null) author = tmp;
            tmp = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.RELEASED, null);
            if (tmp != null) releaseDate = tmp;
            tmp = ElementUtil.getChildValueString(root, WebGridBeanConstants.NAMESPACE, WebGridBeanConstants.DESCRIPTION, null);
            if (tmp != null) description = tmp;
            gbdInput.close();
        } catch (Exception e) {
            throw new GridBeanException("Cannot load GridBean definition.", e);
        }
        long id = System.currentTimeMillis();
        metadata = new WebGridBeanMetadata(id, name, version, releaseDate, author, application.getName(), application.getApplicationVersion(), inputJspFile, outputJspFile, icon, description);
    }
}
