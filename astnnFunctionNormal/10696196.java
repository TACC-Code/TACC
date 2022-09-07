class BackupThread extends Thread {
    private final void setupOutputDir() {
        Controler.outputPath = this.config.controler().getOutputDirectory();
        if (Controler.outputPath.endsWith("/")) {
            Controler.outputPath = Controler.outputPath.substring(0, Controler.outputPath.length() - 1);
        }
        File outputDir = new File(Controler.outputPath);
        if (outputDir.exists()) {
            if (outputDir.isFile()) {
                Gbl.errorMsg("Cannot create output directory. " + Controler.outputPath + " is a file and cannot be replaced by a directory.");
            } else {
                if (outputDir.list().length > 0) {
                    if (this.overwriteFiles) {
                        System.err.println("\n\n\n");
                        System.err.println("#################################################\n");
                        System.err.println("THE CONTROLER WILL OVERWRITE FILES IN:");
                        System.err.println(Controler.outputPath);
                        System.err.println("\n#################################################\n");
                        System.err.println("\n\n\n");
                    } else {
                        Gbl.errorMsg("The output directory " + Controler.outputPath + " exists already but has files in it! Please delete its content or the directory and start again. We will not delete or overwrite any existing files.");
                    }
                }
            }
        } else {
            if (!outputDir.mkdir()) {
                Gbl.errorMsg("The output directory " + Controler.outputPath + " could not be created. Does it's parent directory exist?");
            }
        }
        File tmpDir = new File(getTempPath());
        if (!tmpDir.mkdir() && !tmpDir.exists()) {
            Gbl.errorMsg("The tmp directory " + getTempPath() + " could not be created.");
        }
        File itersDir = new File(Controler.outputPath + "/" + Controler.DIRECTORY_ITERS);
        if (!itersDir.mkdir() && !itersDir.exists()) {
            Gbl.errorMsg("The iterations directory " + (Controler.outputPath + "/" + Controler.DIRECTORY_ITERS) + " could not be created.");
        }
        this.outputDirSetup = true;
    }
}
