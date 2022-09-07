class BackupThread extends Thread {
    @Deprecated
    protected void assembleEJBModuleProducts(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws IOException, XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_EJBMODULE_PRODUCT);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                String ejbModuleJARName = exn.getParameter("jar").valueAsString();
                File src = getFilePath(exn.getDeclaringPluginDescriptor(), ejbModuleJARName);
                File dest = new File(localDestDir, "/" + ejbModuleJARName);
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
                addEJBModuleXMLEntry(ejbModuleJARName, writer);
            }
        }
    }
}
