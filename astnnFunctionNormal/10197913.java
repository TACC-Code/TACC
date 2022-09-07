class BackupThread extends Thread {
    protected void assembleLocaleModuleProductAdaptors(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_LOCALEMODULEPROD_ADPTR);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                for (File src : getAdaptorFiles(exn)) {
                    File dest = new File(localDestDir, "/lib/" + src.getName());
                    dest.getParentFile().mkdirs();
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    FileUtils.copyFile(src, dest);
                }
            }
        }
    }
}
