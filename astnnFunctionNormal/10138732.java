class BackupThread extends Thread {
    public void test_read_write() throws IOException {
        test_read_write(1, "");
        test_read_write(2, "test test test");
        test_read_write(3, "test with letters : ����");
        StringBuffer l_buffer = new StringBuffer();
        for (int i = 0; i < 1000; i++) {
            l_buffer.append("line " + i + "\n");
        }
        test_read_write(4, l_buffer.toString());
        l_buffer = new StringBuffer();
        for (int i = 0; i < 100000; i++) {
            l_buffer.append("line " + i + "\n");
        }
        test_read_write(5, l_buffer.toString());
    }
}
