class BackupThread extends Thread {
    public void test_read_write(String a_index, String a_content, String a_encoding) throws IOException {
        File l_file = getTempFile(1, "test.txt");
        GB_FileTools.writeFile(l_file, a_content, false, a_encoding);
        assertTrue(a_index + ".2" + " - write OK", l_file.canRead());
        String l_newContent = GB_FileTools.readFile(l_file, a_encoding);
        assertEquals(a_index + ".3" + " - read OK", a_content, l_newContent);
        GB_FileTools.writeFile(l_file, a_content, true, a_encoding);
        l_newContent = GB_FileTools.readFile(l_file, a_encoding);
        assertEquals(a_index + ".4" + " - read OK (append)", a_content + a_content, l_newContent);
        l_file.delete();
    }
}
