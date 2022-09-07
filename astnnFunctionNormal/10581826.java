class BackupThread extends Thread {
    public ProcessContentResult process(NodeTransaction transaction) {
        ProcessContentResult result = new ProcessContentResult();
        result.setSuccess(false);
        result.setStatus(CommonTransactionStatusCode.Failed);
        result.getAuditEntries().add(makeEntry("Preparing to retrieve log files..."));
        try {
            result.getAuditEntries().add(makeEntry("Validating transaction..."));
            validateTransaction(transaction);
            result.getAuditEntries().add(makeEntry("Validating required helpers..."));
            SettingServiceProvider settingService = (SettingServiceProvider) getServiceFactory().makeService(SettingServiceProvider.class);
            if (settingService == null) {
                throw new RuntimeException("Unable to obtain SettingServiceProvider");
            }
            CompressionService compressionService = (CompressionService) getServiceFactory().makeService(CompressionService.class);
            if (compressionService == null) {
                throw new RuntimeException("Unable to obtain CompressionService");
            }
            result.getAuditEntries().add(makeEntry("copying log files..."));
            String tempDirName = settingService.getTempDir().getAbsolutePath();
            File workDir = new File(FilenameUtils.concat(tempDirName, "log.tmp"));
            if (!workDir.exists()) {
                if (!workDir.mkdir()) {
                    throw new RuntimeException("Couldn't create work directory " + workDir.getAbsolutePath());
                }
            }
            File logDir = settingService.getLogDir();
            String[] logFiles = logDir.list();
            for (int i = 0; i < logFiles.length; i++) {
                File src = new File(FilenameUtils.concat(logDir.getAbsolutePath(), logFiles[i]));
                FileUtils.copyFileToDirectory(src, workDir);
            }
            result.getAuditEntries().add(makeEntry("Compressing log files..."));
            SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_FORMAT);
            String timeStamp = format.format(new Date());
            String outputFileName = ARCHIVE_NAME + "_" + timeStamp + ".zip";
            String outputFilePath = FilenameUtils.concat(tempDirName, outputFileName);
            result.getAuditEntries().add(makeEntry("Output file: " + outputFilePath));
            compressionService.zip(outputFilePath, workDir.getAbsolutePath());
            File outputFile = new File(outputFilePath);
            if (!outputFile.exists()) {
                throw new RuntimeException("Output file does not exist");
            }
            Document doc = new Document();
            result.getAuditEntries().add(makeEntry("Creating document..."));
            result.getAuditEntries().add(makeEntry("Result: " + outputFile));
            doc.setType(CommonContentType.ZIP);
            doc.setDocumentName(FilenameUtils.getName(outputFile.getAbsolutePath()));
            doc.setContent(FileUtils.readFileToByteArray(outputFile));
            result.getAuditEntries().add(makeEntry("Setting result..."));
            result.setPaginatedContentIndicator(new PaginationIndicator(transaction.getRequest().getPaging().getStart(), transaction.getRequest().getPaging().getCount(), true));
            result.getDocuments().add(doc);
            result.setSuccess(true);
            result.setStatus(CommonTransactionStatusCode.Processed);
            result.getAuditEntries().add(makeEntry("Done: OK"));
        } catch (Exception ex) {
            error(ex);
            ex.printStackTrace();
            result.setSuccess(false);
            result.setStatus(CommonTransactionStatusCode.Failed);
            result.getAuditEntries().add(makeEntry("Error while executing: " + this.getClass().getName() + "Message: " + ex.getMessage()));
        }
        return result;
    }
}
