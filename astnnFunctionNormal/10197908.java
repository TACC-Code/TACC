class BackupThread extends Thread {
    @Deprecated
    protected void assembleJavaModuleProductPlugins(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_JAVAMODULE_PRODUCT_PLUGIN);
        if (exnPt != null) {
            for (Extension exn : pd.getExtensionPoint(EXNPT_JAVAMODULE_PRODUCT_PLUGIN).getConnectedExtensions()) {
                String pluginId = exn.getParameter("target-plugin-id").valueAsString();
                PluginDescriptor targetPD = getManager().getRegistry().getPluginDescriptor(pluginId);
                Parameter exnPtParam = exn.getParameter("extension-point");
                if (exnPtParam == null || exnPtParam.valueAsString() == null) {
                    throw new RuntimeException(exn + " must have a parameter called extension-point with a value");
                }
                String exnPtId = exnPtParam.valueAsString();
                ExtensionPoint targetExnPt = targetPD.getExtensionPoint(exnPtId);
                if (targetExnPt == null) {
                    throw new RuntimeException(targetPD + " must have an extension point " + exnPtId);
                }
                for (Parameter libParam : exnPtParam.getSubParameters("name")) {
                    String defaultValueParam = libParam.valueAsString();
                    String defaultValue = targetExnPt.getParameterDefinition(defaultValueParam).getDefaultValue();
                    if (defaultValue == null) {
                        throw new RuntimeException(targetExnPt + " must have a parameter-def " + defaultValueParam);
                    }
                    File src = getFilePath(targetPD, defaultValue);
                    File dest = new File(localDestDir, "/lib/" + src.getName());
                    dest.getParentFile().mkdirs();
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    FileUtils.copyFile(src, dest);
                }
            }
        }
    }
}
