class BackupThread extends Thread {
    public void testWriteString() {
        StringWriter writer = new StringWriter();
        String result = null;
        try {
            StringUtils.writeString("This is another test string.", writer);
        } catch (IOException e) {
            fail("Unexpected exception testing read/write file.");
        }
        assertEquals("This is another test string.", writer.getBuffer().toString());
    }
}
