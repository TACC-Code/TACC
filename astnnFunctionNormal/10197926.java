class BackupThread extends Thread {
    @Deprecated
    protected void executeRequiredConnectorModuleProducts(PluginDescriptor pd) throws Exception {
        ExtensionPoint abstractExnPt = getAbstractEARPluginDescriptor().getExtensionPoint(EXNPT_CONNECTORMODULE_PRODUCT);
        for (ExtensionPoint exnPt : abstractExnPt.getDescendants()) {
            if (exnPt.getDeclaringPluginDescriptor().getId().equals(pd.getId())) {
                File destDir = new File(getPluginTmpDir(), pd.getId());
                destDir.mkdirs();
                for (Extension exn : exnPt.getConnectedExtensions()) {
                    PluginDescriptor rarPluginDescriptor = exn.getDeclaringPluginDescriptor();
                    String rarFilename = exn.getParameter("rar").valueAsString();
                    File src = getFilePath(rarPluginDescriptor, rarFilename);
                    logger.debug("Copy " + src.getPath() + " to " + destDir.getPath());
                    FileUtils.copyFileToDirectory(src, destDir);
                }
            }
        }
    }
}
