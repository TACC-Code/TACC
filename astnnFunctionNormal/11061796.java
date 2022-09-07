class BackupThread extends Thread {
    @Test
    public void testWrite() throws DITAOTException, SAXException, IOException {
        final File f = new File(tempDir, "test.dita");
        FileUtils.copyFile(new File(srcDir, "test.dita"), f);
        FileUtils.copyFile(new File(srcDir, "code.xml"), new File(tempDir, "code.xml"));
        FileUtils.copyFile(new File(srcDir, "utf-8.xml"), new File(tempDir, "utf-8.xml"));
        final CoderefResolver filter = new CoderefResolver();
        filter.setLogger(new TestUtils.TestLogger());
        filter.write(f.getAbsolutePath());
        TestUtils.resetXMLUnit();
        XMLUnit.setIgnoreWhitespace(true);
        assertXMLEqual(new InputSource(new File(expDir, "test.dita").toURI().toString()), new InputSource(f.toURI().toString()));
    }
}
