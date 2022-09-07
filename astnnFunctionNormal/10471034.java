class BackupThread extends Thread {
    public void process(String sourceDocumentPath) {
        logger.debug("process(%s)", sourceDocumentPath);
        try {
            if (this.model.isEmpty()) return;
            File outTempFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString() + ".pdf");
            FileOutputStream outputStream = new FileOutputStream(outTempFile);
            this.document = new com.itextpdf.text.Document();
            PdfWriter writer = PdfWriter.getInstance(this.document, outputStream);
            if (this.configuration.getBoolean(Configuration.GENERATE_PDF_A)) writer.setPDFXConformance(PdfWriter.PDFA1B);
            this.document.open();
            PdfContentByte finalPdfContent = writer.getDirectContent();
            PdfReader reader = new PdfReader(sourceDocumentPath);
            AcroFields fields = reader.getAcroFields();
            classifyFields(fields);
            for (int page = 1; page <= reader.getNumberOfPages(); page++) {
                String sPageInfo = fields.getField(String.format("PageInfo:%s", page));
                Map<String, PdfCollection> pageInfo;
                if (sPageInfo != null) pageInfo = parsePageInfo(sPageInfo); else pageInfo = new HashMap<String, PdfCollection>();
                PdfImportedPage importedPage = null;
                if (pageInfo.size() > 0) {
                    Iterator<PdfCollection> pageCollectionIterator = pageInfo.values().iterator();
                    boolean pageCreated = false;
                    while (pageCollectionIterator.hasNext()) {
                        PdfCollection collection = pageCollectionIterator.next();
                        if (!this.model.isPropertyACollection(collection.getId())) continue;
                        Collection<Model> modelCollection = this.model.getPropertyAsCollection(collection.getId());
                        Iterator<Model> iterator = modelCollection.iterator();
                        Model currentModel;
                        int currentPage = 0, currentRow = collection.getPageSize();
                        float currentOffset = 0.0f;
                        float offset = calculateOffset(fields, collectionsFields.get(collection.getId()), collection);
                        if (!iterator.hasNext()) {
                            this.document.newPage();
                            importedPage = writer.getImportedPage(reader, page);
                            finalPdfContent.addTemplate(importedPage, 0, 0);
                        }
                        while (iterator.hasNext()) {
                            if (currentRow == collection.getPageSize() && (!pageCreated || collection.isMultipage())) {
                                currentRow = 0;
                                currentOffset = 0;
                                currentPage++;
                                pageCreated = true;
                                this.document.newPage();
                                importedPage = writer.getImportedPage(reader, page);
                                finalPdfContent.addTemplate(importedPage, 0, 0);
                            }
                            if (currentRow == collection.getPageSize()) {
                                if (!collection.isMultipage() && !(pageInfo.size() > 1)) break;
                                currentRow = 0;
                            }
                            currentModel = iterator.next();
                            for (FieldInfo fieldInfo : collectionsFields.get(collection.getId())) {
                                if (fieldInfo.parts[1].equals(collection.getOffsetField()) && !fieldInfo.parts[2].equals("0")) continue;
                                Rectangle fieldRect = null;
                                if (fieldInfo.rect != null) fieldRect = new Rectangle(fieldInfo.rect.left(), fieldInfo.rect.top() - currentOffset, fieldInfo.rect.right(), fieldInfo.rect.bottom() - currentOffset); else fieldRect = new Rectangle(0, currentOffset, 0, currentOffset);
                                fillField(fields, writer, currentModel, fieldInfo.originalName, fieldInfo.originalName + currentRow, fieldInfo.parts[1], fields.getFieldItem(fieldInfo.originalName), fieldRect);
                            }
                            currentOffset += offset;
                            currentRow++;
                        }
                    }
                } else {
                    this.document.newPage();
                    importedPage = writer.getImportedPage(reader, page);
                    finalPdfContent.addTemplate(importedPage, 0, 0);
                    fillPlainFields(page, fields, finalPdfContent, writer);
                }
            }
            this.document.close();
            outputStream.close();
            reader.close();
            reader = new PdfReader(outTempFile.getAbsolutePath());
            outputStream = new FileOutputStream(sourceDocumentPath, false);
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            stamper.setFormFlattening(true);
            stamper.close();
            outputStream.close();
            reader.close();
            if (outTempFile.exists()) outTempFile.delete();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException("Error updating document.");
        }
    }
}
