class BackupThread extends Thread {
    public void testStripReadOnlyParagraph() {
        String fileName = "read_only_stripped.docx";
        String fileInput = inputDirectory + "read_only_text_with_write_enable.docx";
        String expectedResult = expectedDirectory + fileName;
        ZipFile zipFileSource1;
        try {
            zipFileSource1 = new ZipFile(fileInput);
            Package packSource1 = Package.open(zipFileSource1, PackageAccess.ReadWrite);
            WordDocument docxSource1 = new WordDocument(packSource1);
            docxSource1.stripReadOnlyPartOfDocument();
            File destFile = new File(outputDirectory + fileName);
            assertTrue(docxSource1.save(destFile));
            OpenXmlAssert.assertEquals(new File(outputDirectory + fileName), new File(expectedResult));
        } catch (IOException e) {
            logger.error(e);
            fail("cannot open:" + fileInput);
        } catch (OpenXML4JException e) {
            logger.error(e);
            fail("fail" + e.getMessage());
        }
    }
}
