class BackupThread extends Thread {
    @Deprecated
    protected void assembleLocaleModules(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_LOCALEMODULE);
        if (exnPt != null) {
            for (Extension localeModuleExtension : exnPt.getConnectedExtensions()) {
                File localeModulePluginTmpDir = getPluginTmpDir(localeModuleExtension.getDeclaringPluginDescriptor());
                String srcName = localeModuleExtension.getParameter("jar").valueAsString();
                File src = new File(localeModulePluginTmpDir, srcName);
                File dest = new File(localDestDir, "/lib/" + src.getName());
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
            }
        }
    }
}
