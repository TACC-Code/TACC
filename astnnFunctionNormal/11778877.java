class BackupThread extends Thread {
    @Test
    public void testFileContentEqualsStringString() throws IOException {
        File file1 = JUnitFileHelper.getFileA();
        File file2 = JUnitFileHelper.getFileB();
        FileUtils.copyFile(file1, file2);
        assertTrue(FileUtils_1x0.isFileContentsEquals(file1.getCanonicalPath(), file2.getCanonicalPath()));
    }
}
