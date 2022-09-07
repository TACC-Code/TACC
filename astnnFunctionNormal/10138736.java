class BackupThread extends Thread {
    public void test_read_writeUTF8() throws IOException {
        String l_name = "fileUTF8.txt";
        File l_file = FTools.getTempFile(l_name);
        InputStream l_inputStream = this.getClass().getResourceAsStream(l_name);
        if (l_inputStream == null) {
            throw new NullPointerException("Resource not found : " + l_name);
        }
        FTools.copyInputStreamToFile(l_inputStream, l_file);
        String l_txt = FTools.readFile(l_file, ENCODING.UTF8);
        File l_fileTemp = getTempFile(1, "testCopyUTF.txt");
        FTools.writeFile(l_fileTemp, l_txt, false, ENCODING.UTF8);
        String l_txt2 = FTools.readFile(l_fileTemp, ENCODING.UTF8);
        assertEquals(l_txt, l_txt2);
    }
}
