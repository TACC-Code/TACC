class BackupThread extends Thread {
    public void test_read_write(int a_index, String a_content) throws IOException {
        test_read_write(a_index + ".NULL", a_content, null);
        test_read_write(a_index + ".DEF", a_content, ENCODING.DEFAULT);
        test_read_write(a_index + ".ISO", a_content, ENCODING.ISO_8859_1);
        test_read_write(a_index + ".UTF8", a_content, ENCODING.UTF8);
    }
}
