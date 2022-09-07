class BackupThread extends Thread {
    public void testRemoveWriteEnableTagsWithDocumentInMemory() {
        String fileName = "read_only_with_write_parts_removed_as_byte.docx";
        String fileInput = inputDirectory + "read_only_with_write_parts.docx";
        String expectedResult = expectedDirectory + "read_only_with_write_parts_removed.docx";
        ZipFile zipFileSource1;
        try {
            zipFileSource1 = new ZipFile(fileInput);
            Package packSource1 = Package.open(zipFileSource1, PackageAccess.ReadWrite);
            WordDocument docxSource1 = new WordDocument(packSource1);
            docxSource1.removeWriteEnabledTags();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            assertTrue(docxSource1.save(outputStream));
            byte fileAsByte[] = outputStream.toByteArray();
            FileOutputStream file = new FileOutputStream(new File(outputDirectory + fileName));
            file.write(fileAsByte);
            file.close();
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
