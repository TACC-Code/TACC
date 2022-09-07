class BackupThread extends Thread {
    @Deprecated
    protected void assembleLocaleModuleProducts(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_LOCALEMODULE_PRODUCT);
        if (exnPt != null) {
            for (Extension localeModuleProductExtension : exnPt.getConnectedExtensions()) {
                String srcName = localeModuleProductExtension.getParameter("jar").valueAsString();
                File src = getFilePath(localeModuleProductExtension.getDeclaringPluginDescriptor(), srcName);
                File dest = new File(localDestDir, "/lib/" + src.getName());
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
            }
        }
    }
}
