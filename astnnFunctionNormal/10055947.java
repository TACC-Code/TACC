class BackupThread extends Thread {
    public boolean parseCommandLine(String[] args) {
        if (args[0].indexOf("-i") == 0) {
            String inTypeS = args[0].substring(2);
            inType = IOTypeHolder.instance().getIOType(inTypeS.toUpperCase());
            if (inType == null) {
                logger.error("Input type '" + inTypeS + "' not defined.");
                return false;
            }
        }
        inputFile = args[1];
        if (args[2].indexOf("-o") == 0) {
            String outTypeS = args[2].substring(2);
            outType = IOTypeHolder.instance().getIOType(outTypeS.toUpperCase());
            if (outType == null) {
                logger.error("Output type '" + outTypeS + "' not defined.");
                return false;
            }
        }
        String outputFile = args[3];
        String descNamesURL = args[4];
        int descOutType = DescSelectionWriter.MOL_AND_DESCRIPTORS;
        String dOutString = args[5];
        if (dOutString.equalsIgnoreCase("flat")) {
            descOutType = DescSelectionWriter.DESCRIPTORS;
        } else {
            descOutType = DescSelectionWriter.MOL_AND_DESCRIPTORS;
        }
        delimiter = args[6];
        DescriptorFilter descFilter = null;
        try {
            descFilter = (DescriptorFilter) FilterFactory.instance().getFilter("DescriptorFilter");
        } catch (FilterException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        if (descFilter == null) {
            logger.error("Filter: DescriptorFilter could not be found.");
            System.exit(1);
        }
        descFilter.init(descNamesURL, false);
        DescSelectionWriter dsw = null;
        try {
            dsw = (DescSelectionWriter) ProcessFactory.instance().getProcess("DescriptorSelectionWriter");
            processPipe = (ProcessPipe) ProcessFactory.instance().getProcess("ProcessPipe");
        } catch (JOEProcessException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        List desc2write = ResourceLoader.readLines(descNamesURL, false);
        if (desc2write == null) {
            logger.error("Can't load " + descNamesURL);
            System.exit(1);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("select " + desc2write.size() + " descriptors:" + desc2write);
        }
        try {
            dsw.init(outputFile, outType, desc2write, descOutType);
            dsw.setDelimiter(delimiter);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        processPipe.addProcess(dsw, descFilter);
        return true;
    }
}
