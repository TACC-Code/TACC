class BackupThread extends Thread {
    @Test
    public void testRemoveFragmentFromZipTypeDigitalObject() {
        printTestTitle("Test removeFragmentFromZipTypeDigitalObject()");
        DigitalObject result = DigitalObjectUtils.createZipTypeDigitalObject(removeZip, "removeFragmentTest.zip", false, false, true);
        printDigOb(result);
        DigitalObject removeResult = DigitalObjectUtils.removeFragment(result, new String("insertedFiles\\images\\laptop.gif"), false);
        FileUtils.writeInputStreamToFile(removeResult.getContent().read(), new File(work_folder, removeResult.getTitle()));
        printDigOb(removeResult);
    }
}
