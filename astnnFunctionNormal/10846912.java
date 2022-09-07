class BackupThread extends Thread {
    private File createExternalizedBackupFile(URL backup) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(backup.openStream()));
        File result = TempFileFactory.get().createTempFile("pdash-backup", ".zip");
        result.deleteOnExit();
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(result)));
        ExternalResourceManager extMgr = ExternalResourceManager.getInstance();
        ZipEntry e;
        while ((e = zipIn.getNextEntry()) != null) {
            String filename = e.getName();
            if (extMgr.isArchivedItem(filename)) continue;
            ZipEntry eOut = new ZipEntry(filename);
            eOut.setTime(e.getTime());
            zipOut.putNextEntry(eOut);
            FileUtils.copyFile(zipIn, zipOut);
            zipOut.closeEntry();
        }
        zipIn.close();
        extMgr.addExternalResourcesToBackup(zipOut);
        zipOut.finish();
        zipOut.close();
        return result;
    }
}
