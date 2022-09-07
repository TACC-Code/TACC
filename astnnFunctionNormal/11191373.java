class BackupThread extends Thread {
    private void downloadZipFile(FILE_TYPE fileType) throws DownloadException {
        ServletOutputStream os = null;
        FileInputStream fis = null;
        File outputZipFile = null;
        String outputFilename = project.getName() + ".zip";
        HttpServletResponse response = (HttpServletResponse) extCtx.getResponse();
        response.setContentType("application/zip");
        response.addHeader("Content-disposition", "attachment; filename=\"" + outputFilename + "\"");
        try {
            outputZipFile = File.createTempFile("zippedTranslation", ".tmp");
            switch(fileType) {
                case TRANSLATIONS:
                    project.createTranslations(outputZipFile);
                    break;
                case BACKUP:
                    project.createBackup(outputZipFile);
                    break;
            }
            os = response.getOutputStream();
            fis = new FileInputStream(outputZipFile);
            byte buffer[] = new byte[4096];
            int read = 0;
            while ((read = fis.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
            os.flush();
            os.close();
            fis.close();
            facesContext.responseComplete();
        } catch (Exception e) {
            throw new DownloadException("Download of zip file failed", e);
        } finally {
            if (outputZipFile != null) {
                boolean success = outputZipFile.delete();
                if (!success) {
                    log.error("\nCouldn't delete temp file after download.\n");
                }
            }
        }
    }
}
