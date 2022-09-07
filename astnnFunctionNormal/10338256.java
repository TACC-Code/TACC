class BackupThread extends Thread {
    public void run() {
        Document configuration = null;
        MainWindow desktop = null;
        logger.finest("Checking for old temporary files...");
        try {
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File remainingTmpFiles[] = tempDir.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.matches("mzmine.*\\.scans");
                }
            });
            for (File remainingTmpFile : remainingTmpFiles) {
                if (!remainingTmpFile.canWrite()) continue;
                RandomAccessFile rac = new RandomAccessFile(remainingTmpFile, "rw");
                FileLock lock = rac.getChannel().tryLock();
                rac.close();
                if (lock != null) {
                    logger.finest("Removing unused file " + remainingTmpFile);
                    remainingTmpFile.delete();
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while checking for old temporary files", e);
        }
        SAXReader reader = new SAXReader();
        try {
            configuration = reader.read(CONFIG_FILE);
        } catch (DocumentException e1) {
            if (CONFIG_FILE.exists()) {
                logger.log(Level.WARNING, "Error parsing the configuration file " + CONFIG_FILE + ", loading default configuration", e1);
            }
            try {
                configuration = reader.read(DEFAULT_CONFIG_FILE);
            } catch (DocumentException e2) {
                logger.log(Level.SEVERE, "Error parsing the default configuration file " + DEFAULT_CONFIG_FILE, e2);
                System.exit(1);
            }
        }
        Element configRoot = configuration.getRootElement();
        logger.info("Starting MZmine 2");
        logger.info("Loading core classes..");
        MZmineCore.preferences = new MZminePreferences();
        TaskControllerImpl taskController = new TaskControllerImpl();
        projectManager = new ProjectManagerImpl();
        desktop = new MainWindow();
        help = new HelpImpl();
        MZmineCore.taskController = taskController;
        MZmineCore.desktop = desktop;
        logger.finer("Initializing core classes..");
        projectManager.initModule();
        desktop.initModule();
        taskController.initModule();
        logger.finer("Loading modules");
        moduleSet = new Vector<MZmineModule>();
        Iterator<Element> modIter = configRoot.element(MODULES_ELEMENT_NAME).elementIterator(MODULE_ELEMENT_NAME);
        while (modIter.hasNext()) {
            Element moduleElement = modIter.next();
            String className = moduleElement.attributeValue(CLASS_ATTRIBUTE_NAME);
            loadModule(className);
        }
        MZmineCore.initializedModules = moduleSet.toArray(new MZmineModule[0]);
        try {
            if (CONFIG_FILE.exists()) loadConfiguration(CONFIG_FILE);
        } catch (DocumentException e) {
            logger.log(Level.WARNING, "Error while loading module configuration", e);
        }
        ShutDownHook shutDownHook = new ShutDownHook();
        Runtime.getRuntime().addShutdownHook(shutDownHook);
        logger.finest("Showing main window");
        desktop.setVisible(true);
        desktop.setStatusBarText("Welcome to MZmine 2!");
    }
}
