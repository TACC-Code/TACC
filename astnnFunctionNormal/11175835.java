class BackupThread extends Thread {
    @Test
    public void testModify() throws URISyntaxException {
        List<File> fileList = FileUtils.listAllFilesAndFolders(FILES_FOR_MODIFICATION, new ArrayList<File>());
        fileList.remove(FLOPPY_IMAGE);
        FormatRegistry format = FormatRegistryFactory.getFormatRegistry();
        DigitalObject inputDigObj = new DigitalObject.Builder(Content.byReference(FLOPPY_IMAGE)).title(FLOPPY_IMAGE.getName()).format(format.createExtensionUri(FileUtils.getExtensionFromFile(FLOPPY_IMAGE))).build();
        ModifyResult result = FLOPPY_IMAGE_MODIFY.modify(inputDigObj, format.createExtensionUri(FileUtils.getExtensionFromFile(FLOPPY_IMAGE)), null);
        System.out.println("Got report: " + result.getReport());
        DigitalObject digObjres = result.getDigitalObject();
        assertTrue("Resulting DigitalObject should not be NULL", digObjres != null);
        System.out.println("DigitalObject: " + digObjres);
        File out = new File(OUT_DIR, digObjres.getTitle());
        FileUtils.writeInputStreamToFile(digObjres.getContent().read(), out);
        System.out.println("Please find the floppy image here: " + out.getAbsolutePath());
    }
}
