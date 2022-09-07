class BackupThread extends Thread {
    public void executeIntactMojo() throws MojoExecutionException, MojoFailureException {
        getLog().info("DrExportMojo in action");
        File drExportFile = getUniprotLinksFile();
        getLog().info("DR export (uniprot links) will be saved in: " + drExportFile);
        if (drExportFile.exists() && !overwrite) {
            throw new MojoExecutionException("DR Export file already exist and overwrite is set to false: " + drExportFile);
        }
        new MemoryMonitor();
        int eligibleProteinsCount = 0;
        try {
            FileWriter fw = new FileWriter(drExportFile);
            BufferedWriter out = new BufferedWriter(fw);
            Set<String> proteinEligible = null;
            int firstResult = 0;
            int maxResults = 200;
            if (isExportLimited()) {
                maxResults = Math.min(200, maxProteinsToExport);
                getLog().info("Limited export. Only " + maxProteinsToExport + " will be checked for eligibility");
            }
            int totalUniprotProteins = IntactContext.getCurrentInstance().getDataContext().getDaoFactory().getProteinDao().countUniprotProteinsInvolvedInInteractions();
            IntactContext.getCurrentInstance().getDataContext().commitTransaction();
            getLog().debug("Uniprot proteins involved in interactions: " + totalUniprotProteins);
            do {
                IntactContext.getCurrentInstance().getDataContext().beginTransaction();
                DRLineExport exporter = new DRLineExport(getConfig(), getOutputPrintStream());
                proteinEligible = exporter.getEligibleProteins(firstResult, maxResults);
                if (proteinEligible != null) {
                    eligibleProteinsCount = eligibleProteinsCount + proteinEligible.size();
                    getLog().debug(proteinEligible.size() + " protein(s) selected for export using a paginated query. First result: " + firstResult);
                    writeToFile(proteinEligible, out);
                }
                IntactContext.getCurrentInstance().getDataContext().commitTransaction();
                firstResult = firstResult + maxResults;
                if (isExportLimited() && firstResult >= maxProteinsToExport) {
                    break;
                }
            } while (firstResult <= totalUniprotProteins);
            getLog().info("Eligible proteins found: " + eligibleProteinsCount);
            writeLineToSummary("DR Lines: " + eligibleProteinsCount);
        } catch (Exception e) {
            throw new MojoExecutionException("Problem writing eligible proteins", e);
        }
        if (eligibleProteinsCount == 0) {
            throw new MojoFailureException("No eligible proteins to export found");
        }
    }
}
