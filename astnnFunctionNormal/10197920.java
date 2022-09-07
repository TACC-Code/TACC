class BackupThread extends Thread {
    protected void assembleWARModuleProductsPlugins(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws IOException, XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_WARMODULEPROD_ADPTR);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                for (File src : getAdaptorFiles(exn)) {
                    File dest = new File(localDestDir, "/" + src.getName());
                    dest.getParentFile().mkdirs();
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    FileUtils.copyFile(src, dest);
                    String webURI = exn.getParameter("web-uri").valueAsString();
                    String contextRoot = exn.getParameter("context-root").valueAsString();
                    addWebModules(webURI, contextRoot, writer);
                }
            }
        }
    }
}
