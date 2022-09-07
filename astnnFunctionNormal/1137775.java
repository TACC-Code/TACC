class BackupThread extends Thread {
    public void testNullOutputCopy() throws Exception {
        byte[] bytes = BYTES;
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream output = null;
        Copier.copy(input, output, 8, 0);
        char[] chars = CHARS;
        CharArrayReader reader = new CharArrayReader(chars);
        CharArrayWriter writer = null;
        Copier.copy(reader, writer, 8, 0);
    }
}
