class BackupThread extends Thread {
    private File createPackage(IProgressMonitor monitor, File tempDirectory, InstallConfiguration installConfiguration) throws InvocationTargetException {
        monitor.beginTask("create package", 7);
        File randomDirectory = new File(FileSystemUtils.getDir(RegiloInstallerPlugin.INSTALL_DIR), UUID.randomUUID().toString());
        randomDirectory.mkdir();
        monitor.subTask("creating directories");
        File core = new File(tempDirectory, "core");
        String[] coreFiles = core.list();
        File modules = new File(tempDirectory, "modules");
        String[] modulesFiles = modules.list();
        File profile = new File(tempDirectory, "profile");
        File custom = new File(tempDirectory, "custom");
        String[] customFiles = custom.list();
        File theme = new File(tempDirectory, "theme");
        String[] themeFiles = theme.list();
        File drupalDir = new File(randomDirectory, "drupal");
        File drupalSitesDir = new File(drupalDir, "sites");
        File drupalProfileDir = new File(drupalDir, "profiles");
        File drupalAllDir = new File(drupalSitesDir, "all");
        drupalAllDir.mkdirs();
        File drupalDefaultDir = new File(drupalSitesDir, "default");
        drupalDefaultDir.mkdirs();
        File drupalModulesDir = new File(drupalAllDir, "modules");
        drupalModulesDir.mkdirs();
        File drupalThemesDir = new File(drupalAllDir, "themes");
        drupalThemesDir.mkdirs();
        File drupalFilesDir = new File(drupalDefaultDir, "files");
        drupalFilesDir.mkdirs();
        monitor.worked(1);
        try {
            monitor.subTask("configuring core");
            File coreFile = new File(core, coreFiles[0]);
            FileUtils.copyDirectory(coreFile, drupalDir);
            monitor.worked(1);
            monitor.subTask("configuring modules");
            for (int i = 0; i < modulesFiles.length; i++) {
                File moduleFile = new File(modules, modulesFiles[i]);
                File moduleDir = new File(drupalModulesDir, modulesFiles[i]);
                if (moduleFile.isDirectory()) {
                    FileUtils.copyDirectory(moduleFile, moduleDir);
                }
            }
            monitor.worked(1);
            monitor.subTask("configuring custom module");
            for (int i = 0; i < customFiles.length; i++) {
                File customFile = new File(custom, customFiles[i]);
                File moduleDir = new File(drupalModulesDir, customFiles[i]);
                if (customFile.isDirectory()) {
                    FileUtils.copyDirectory(customFile, moduleDir);
                }
            }
            monitor.worked(1);
            monitor.subTask("configuring theme module");
            File themeFile = new File(theme, themeFiles[0]);
            String themeName = installConfiguration.getThemeName();
            File themeDir = new File(drupalThemesDir, themeName);
            FileUtils.copyDirectory(themeFile, themeDir);
            monitor.worked(1);
            monitor.subTask("configuring settings file");
            File defaultSettings = new File(drupalDefaultDir, "default.settings.php");
            File settings = new File(drupalDefaultDir, "settings.php");
            FileUtils.copyFile(defaultSettings, settings);
            monitor.worked(1);
            monitor.subTask("configuring installation file");
            File profileDir = new File(profile, installConfiguration.getProfileName());
            File unattendedFile = new File(profileDir, "unattended_install.php");
            FileUtils.copyFileToDirectory(unattendedFile, drupalDir);
            profileDir = new File(profileDir, installConfiguration.getProfileName());
            drupalProfileDir = new File(drupalProfileDir, installConfiguration.getProfileName());
            FileUtils.copyDirectory(profileDir, drupalProfileDir);
            createConfigurationFile(drupalDir, installConfiguration);
            monitor.worked(1);
        } catch (IOException e) {
            throw new InvocationTargetException(e);
        } finally {
            monitor.done();
        }
        return drupalDir;
    }
}
