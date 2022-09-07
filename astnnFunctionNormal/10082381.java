class BackupThread extends Thread {
    public void installPlugin(File file, Project parentProject, boolean installToUnpackedPluginDirectory) throws MavenException {
        log.debug("Using plugin dependency: " + file);
        try {
            if (installToUnpackedPluginDirectory) {
                FileUtils.copyFileToDirectory(file, unpackedPluginsDir);
            }
            String pluginName = file.getCanonicalFile().getName();
            pluginName = pluginName.substring(0, pluginName.indexOf(".jar"));
            if (!isLoaded(pluginName)) {
                File unpackedPluginDir = unpackPlugin(pluginName, file);
                if (unpackedPluginDir != null) {
                    JellyScriptHousing housing = createPluginHousing(unpackedPluginDir);
                    if (housing == null) {
                        throw new MavenException("Not a valid plugin file: " + file);
                    }
                    housing.parse(transientMapper);
                    housing.parse(mapper);
                    if (installToUnpackedPluginDirectory) {
                        cacheManager.registerPlugin(pluginName, housing);
                        housing.parse(cacheManager);
                        cacheManager.saveCache(unpackedPluginsDir);
                    }
                } else {
                    throw new MavenException("Not a valid JAR file: " + file);
                }
            }
        } catch (IOException e) {
            throw new MavenException("Error installing plugin", e);
        }
    }
}
