class BackupThread extends Thread {
    @Test
    public void testInputStreamContentEquals() throws IOException {
        File file1 = JUnitFileHelper.getFileA();
        File file2 = JUnitFileHelper.getFileB();
        FileUtils.copyFile(file1, file2);
        FileInputStream is1 = new FileInputStream(file1);
        FileInputStream is2 = new FileInputStream(file2);
        assertTrue(FileUtils_1x0.isInputStreamContentEquals(is1, is2));
        is1.close();
        is2.close();
    }
}
