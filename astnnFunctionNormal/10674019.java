class BackupThread extends Thread {
    public void testRemoveWriteEnableTags() {
        String fileName = "read_only_with_write_parts_removed.docx";
        String fileInput = inputDirectory + "read_only_with_write_parts.docx";
        String expectedResult = expectedDirectory + fileName;
        ZipFile zipFileSource1;
        try {
            zipFileSource1 = new ZipFile(fileInput);
            Package packSource1 = Package.open(zipFileSource1, PackageAccess.ReadWrite);
            WordDocument docxSource1 = new WordDocument(packSource1);
            docxSource1.removeWriteEnabledTags();
            File destFile = new File(outputDirectory + fileName);
            assertTrue(docxSource1.save(destFile));
            OpenXmlAssert.assertEquals(new File(outputDirectory + fileName), new File(expectedResult));
        } catch (IOException e) {
            logger.error(e);
            fail("cannot open:" + fileInput);
        } catch (OpenXML4JException e) {
            logger.error(e);
            fail("failed" + e.getMessage());
        }
    }
}
