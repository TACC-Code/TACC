class BackupThread extends Thread {
    private int verifyCheckPropertiesFile(CheckPropertiesFile checkPropertiesFile) {
        int result = INSTALL;
        try {
            URL propURL = ConfigurationUtils.locate(checkPropertiesFile.getResourceName());
            PropertiesConfiguration prop = new PropertiesConfiguration(propURL);
            Log.write("There is a configuration file", Log.INFO, "verifyCheckPropertiesFile", this.getClass());
            if (StringUtils.isNotEmpty(checkPropertiesFile.getProperty()) && StringUtils.isNotEmpty(checkPropertiesFile.getValueToReinstall())) {
                Log.write(checkPropertiesFile.getProperty() + "=" + prop.getString(checkPropertiesFile.getProperty()), Log.INFO, "verifyCheckPropertiesFile", this.getClass());
            }
            if (!prop.getString(checkPropertiesFile.getProperty()).equalsIgnoreCase(checkPropertiesFile.getValueToReinstall())) {
                Log.write("The " + checkPropertiesFile.getResourceName() + " properties file doesn't want to reinstall", Log.INFO, "verifyCheckPropertiesFile", this.getClass());
                result = DO_NOTHING;
                VersionNumber installedVersion = new VersionNumber(prop.getString(Constants.INSTALLED_VERSION_PROPERTY, "0.0.0"));
                PropertiesConfiguration templateProp = new PropertiesConfiguration(this.getServletContext().getResource(checkPropertiesFile.getUpdateTemplate()));
                VersionNumber updateVersion = new VersionNumber(templateProp.getString(Constants.INSTALLED_VERSION_PROPERTY, "0.0.0"));
                if (installedVersion.toLong() < updateVersion.toLong()) {
                    result = UPDATE;
                }
            }
        } catch (Exception e) {
            Log.write("No properties file, or it cannot be read (" + ExceptionUtils.getFullStackTrace(e) + ")", Log.INFO, "verifyCheckPropertiesFile", this.getClass());
        }
        return result;
    }
}
