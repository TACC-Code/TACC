class BackupThread extends Thread {
    public void run() {
        setStatus(TaskStatus.PROCESSING);
        logger.info("Started parsing file " + fileName);
        try {
            if ((!fileName.exists()) || (!fileName.canRead())) {
                throw new Exception("Parsing Cancelled, file does not exist or is not readable");
            }
            FileInputStream fis = new FileInputStream(fileName);
            InputStream finalStream = fis;
            byte b[] = new byte[32];
            fis.read(b);
            String firstLine = new String(b);
            if (!firstLine.contains("<?xml")) {
                FileChannel fc = fis.getChannel();
                fc.position(0);
                ZipInputStream zis = new ZipInputStream(fis);
                zis.getNextEntry();
                finalStream = zis;
            } else {
                FileChannel fc = fis.getChannel();
                fc.position(0);
            }
            Hashtable<String, RawDataFile> dataFilesIDMap = new Hashtable<String, RawDataFile>();
            for (RawDataFile file : MZmineCore.getCurrentProject().getDataFiles()) {
                dataFilesIDMap.put(file.getName(), file);
            }
            peakListOpenHander = new PeakListOpenHandler_2_0(dataFilesIDMap);
            buildingPeakList = peakListOpenHander.readPeakList(finalStream);
        } catch (Throwable e) {
            if (status == TaskStatus.PROCESSING) setStatus(TaskStatus.ERROR);
            errorMessage = e.toString();
            e.printStackTrace();
            return;
        }
        MZmineProject currentProject = MZmineCore.getCurrentProject();
        currentProject.addPeakList(buildingPeakList);
        logger.info("Finished parsing " + fileName);
        setStatus(TaskStatus.FINISHED);
    }
}
