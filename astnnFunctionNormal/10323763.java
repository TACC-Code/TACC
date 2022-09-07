class BackupThread extends Thread {
    public void testReaderCopy() throws Exception {
        char[] chars = CHARS;
        CharArrayReader reader = new CharArrayReader(chars);
        CharArrayWriter writer = new CharArrayWriter();
        Copier.copy(reader, writer, 8, -1);
        assertTrue(Arrays.equals(chars, writer.toCharArray()));
    }
}
