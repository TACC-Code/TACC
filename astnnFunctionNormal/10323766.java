class BackupThread extends Thread {
    public void testExceptions() throws Exception {
        byte[] bytes = BYTES;
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IllegalArgumentException iae = null;
        try {
            Copier.copy(input, output, -1, -1);
        } catch (IllegalArgumentException e) {
            iae = e;
        }
        assertNotNull(iae);
        char[] chars = CHARS;
        CharArrayReader reader = new CharArrayReader(chars);
        CharArrayWriter writer = new CharArrayWriter();
        iae = null;
        try {
            Copier.copy(reader, writer, -1, -1);
        } catch (IllegalArgumentException e) {
            iae = e;
        }
        assertNotNull(iae);
    }
}
