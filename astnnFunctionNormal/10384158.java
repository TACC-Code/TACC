class BackupThread extends Thread {
    public static void main(String[] args) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.addDeveloperExtension(PdfDeveloperExtension.ADOBE_1_7_EXTENSIONLEVEL3);
        document.open();
        writer.addJavaScript(Utilities.readFileToString(JS));
        RichMediaAnnotation richMedia = new RichMediaAnnotation(writer, new Rectangle(36, 560, 561, 760));
        PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(writer, RESOURCE, "FestivalCalendar2.swf", null);
        PdfIndirectReference asset = richMedia.addAsset("FestivalCalendar2.swf", fs);
        RichMediaConfiguration configuration = new RichMediaConfiguration(PdfName.FLASH);
        RichMediaInstance instance = new RichMediaInstance(PdfName.FLASH);
        instance.setAsset(asset);
        configuration.addInstance(instance);
        PdfIndirectReference configurationRef = richMedia.addConfiguration(configuration);
        RichMediaActivation activation = new RichMediaActivation();
        activation.setConfiguration(configurationRef);
        richMedia.setActivation(activation);
        PdfAnnotation richMediaAnnotation = richMedia.createAnnotation();
        richMediaAnnotation.setFlags(PdfAnnotation.FLAGS_PRINT);
        writer.addAnnotation(richMediaAnnotation);
        String[] days = new String[] { "2011-10-12", "2011-10-13", "2011-10-14", "2011-10-15", "2011-10-16", "2011-10-17", "2011-10-18", "2011-10-19" };
        for (int i = 0; i < days.length; i++) {
            Rectangle rect = new Rectangle(36 + (65 * i), 765, 100 + (65 * i), 780);
            PushbuttonField button = new PushbuttonField(writer, rect, "button" + i);
            button.setBackgroundColor(new GrayColor(0.75f));
            button.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
            button.setTextColor(GrayColor.GRAYBLACK);
            button.setFontSize(12);
            button.setText(days[i]);
            button.setLayout(PushbuttonField.LAYOUT_ICON_LEFT_LABEL_RIGHT);
            button.setScaleIcon(PushbuttonField.SCALE_ICON_ALWAYS);
            button.setProportionalIcon(true);
            button.setIconHorizontalAdjustment(0);
            PdfFormField field = button.getField();
            RichMediaCommand command = new RichMediaCommand(new PdfString("getDateInfo"));
            command.setArguments(new PdfString(days[i]));
            RichMediaExecuteAction action = new RichMediaExecuteAction(richMediaAnnotation.getIndirectReference(), command);
            field.setAction(action);
            writer.addAnnotation(field);
        }
        TextField text = new TextField(writer, new Rectangle(36, 785, 559, 806), "date");
        text.setOptions(TextField.READ_ONLY);
        writer.addAnnotation(text.getTextField());
        document.close();
    }
}
