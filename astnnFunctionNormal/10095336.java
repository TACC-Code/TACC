class BackupThread extends Thread {
    private final void setupOutputDir() {
        outputPath = Gbl.getConfig().getParam(CONFIG_MODULE, CONFIG_OUTPUT_DIRECTORY);
        if (outputPath.endsWith("/")) {
            outputPath = outputPath.substring(0, outputPath.length() - 1);
        }
        File outputDir = new File(outputPath);
        if (outputDir.exists()) {
            if (outputDir.isFile()) {
                Gbl.errorMsg("Cannot create output directory. " + outputPath + " is a file and cannot be replaced by a directory.");
            } else {
                if (outputDir.list().length > 0) {
                    if (this.overwriteFiles) {
                        System.err.println("\n\n\n");
                        System.err.println("#################################################\n");
                        System.err.println("THE CONTROLER WILL OVERWRITE FILES IN:");
                        System.err.println(outputPath);
                        System.err.println("\n#################################################\n");
                        System.err.println("\n\n\n");
                    } else {
                        Gbl.errorMsg("The output directory " + outputPath + " exists already but has files in it! Please delete its content or the directory and start again. We will not delete or overwrite any existing files.");
                    }
                }
            }
        } else {
            if (!outputDir.mkdir()) {
                Gbl.errorMsg("The output directory " + outputPath + " could not be created. Does its parent directory exist?");
            }
        }
        File tmpDir = new File(getTempPath());
        if (!tmpDir.mkdir() && !tmpDir.exists()) {
            Gbl.errorMsg("The tmp directory " + getTempPath() + " could not be created.");
        }
        File itersDir = new File(outputPath + "/" + DIRECTORY_ITERS);
        if (!itersDir.mkdir() && !itersDir.exists()) {
            Gbl.errorMsg("The iterations directory " + (outputPath + "/" + DIRECTORY_ITERS) + " could not be created.");
        }
        SOCNET_OUT_DIR = outputPath + "/" + DIRECTORY_SN;
        File snDir = new File(SOCNET_OUT_DIR);
        if (!snDir.mkdir() && !snDir.exists()) {
            Gbl.errorMsg("The iterations directory " + (outputPath + "/" + DIRECTORY_SN) + " could not be created.");
        }
        this.outputDirSetup = true;
    }
}
