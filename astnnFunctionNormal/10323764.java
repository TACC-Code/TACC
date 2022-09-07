class BackupThread extends Thread {
    public void testLimitCopy() throws Exception {
        byte[] bytes = BYTES;
        int sz = bytes.length - 4;
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Copier.copy(input, output, 8, sz);
        byte[] result = new byte[sz];
        System.arraycopy(bytes, 0, result, 0, sz);
        assertTrue(Arrays.equals(result, output.toByteArray()));
        char[] chars = CHARS;
        sz = chars.length - 4;
        CharArrayReader reader = new CharArrayReader(chars);
        CharArrayWriter writer = new CharArrayWriter();
        Copier.copy(reader, writer, 8, sz);
        char[] resultchars = new char[sz];
        System.arraycopy(chars, 0, resultchars, 0, sz);
        assertTrue(Arrays.equals(resultchars, writer.toCharArray()));
    }
}
