class BackupThread extends Thread {
    @Deprecated
    protected void assembleWARModuleProducts(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws IOException, XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_WARMODULEPRODUCT);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                String webURI = exn.getParameter("web-uri").valueAsString();
                File src = getFilePath(exn.getDeclaringPluginDescriptor(), webURI);
                File dest = new File(localDestDir, "/" + src.getName());
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
                String contextRoot = exn.getParameter("context-root").valueAsString();
                addWebModules(webURI, contextRoot, writer);
            }
        }
    }
}
