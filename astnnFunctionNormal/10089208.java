class BackupThread extends Thread {
    private void assertSameContent(String expected, FileObject file) throws Exception {
        assertTrue(file.exists());
        assertSame(FileType.FILE, file.getType());
        final byte[] expectedBin = expected.getBytes("utf-8");
        final FileContent content = file.getContent();
        System.out.println("file: " + file.getURL() + " type: " + file.getType());
        System.out.println("content: " + content + " map: " + content.getAttributes());
        Map map = content.getAttributes();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) (it.next());
        }
        assertEquals("same content length", expectedBin.length, content.getSize());
        final InputStream instr = content.getInputStream();
        final ByteArrayOutputStream outstr;
        try {
            outstr = new ByteArrayOutputStream(expectedBin.length);
            final byte[] buffer = new byte[256];
            int nread = 0;
            while (nread >= 0) {
                outstr.write(buffer, 0, nread);
                nread = instr.read(buffer);
            }
        } finally {
            instr.close();
        }
        assertTrue("same binary content", Arrays.equals(expectedBin, outstr.toByteArray()));
    }
}
