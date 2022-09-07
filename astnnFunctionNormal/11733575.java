class BackupThread extends Thread {
    public CreatePDF(PdfWriter writer, String fieldname, boolean readOnly) {
        this.writer = writer;
        this.fieldname = fieldname;
        this.readOnly = readOnly;
    }
}
