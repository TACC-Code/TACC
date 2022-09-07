class BackupThread extends Thread {
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        int n = writer.getNewObjectNumber(reader, number, generation);
        os.write(PdfEncodings.convertToBytes(new StringBuffer().append(n).append(" 0 R").toString(), null));
    }
}
