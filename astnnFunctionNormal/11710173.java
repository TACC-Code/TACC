class BackupThread extends Thread {
    @Test
    public void testCreateZipTypeDigObFolder() {
        printTestTitle("Test createZipTypeDigOb() from FOLDER");
        DigitalObject result = DigitalObjectUtils.createZipTypeDigitalObject(testFolder, testFolder.getName(), true, false, true);
        assertTrue("DigitalObject should NOT be NULL!", result != null);
        printDigOb(result);
        File resultFile = new File(work_folder, result.getTitle());
        FileUtils.writeInputStreamToFile(result.getContent().read(), resultFile);
        System.out.println("Result size: " + resultFile.length());
    }
}
