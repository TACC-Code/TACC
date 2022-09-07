class BackupThread extends Thread {
    private void deployPropertiesFile(InstallPropertiesFile installPropertiesFile, String configPath, Map<String, String> parameters) throws FileNotFoundException, IOException, ConfigurationException {
        Log.write("ENTER - Deploying " + installPropertiesFile.getInstalledFileName() + " to " + configPath, Log.INFO, "deployPropertiesFile", this.getClass());
        WebContext ctx = WebContextFactory.get();
        InputStream configStream = ctx.getServletContext().getResourceAsStream(installPropertiesFile.getTemplate());
        if (configStream != null) {
            Util.copyStreamToFile(configStream, configPath + "/" + installPropertiesFile.getInstalledFileName());
        } else {
            Log.write("Configuration file " + configPath + "/" + installPropertiesFile.getInstalledFileName() + " not found on WAR or cannot be read", Log.WARN, "deployPropertiesFile", this.getClass());
            throw new FileNotFoundException("Configuration file " + installPropertiesFile.getTemplate() + " not found on WAR or cannot be read");
        }
        PropertiesConfiguration propConfiguration = new PropertiesConfiguration(configPath + "/" + installPropertiesFile.getInstalledFileName());
        for (String property : parameters.keySet()) {
            String value = parameters.get(property);
            Log.write("Setting " + property + "=" + value, Log.INFO, "deployPropertiesFile", this.getClass());
            propConfiguration.setProperty(property, value);
        }
        propConfiguration.save();
        Log.write("EXIT", Log.INFO, "deployPropertiesFile", this.getClass());
    }
}
