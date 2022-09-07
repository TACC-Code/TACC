class BackupThread extends Thread {
    public void testReadWriteFile() {
        File file = null;
        try {
            String string = "This is a test string to write to a file.";
            file = File.createTempFile("stringutils-test", "tst");
            StringUtils.writeFile(string, file);
            String result = StringUtils.readFile(file);
            assertEquals(string, result);
        } catch (IOException e) {
            fail("Unexpected exception testing read/write file.");
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }
}
