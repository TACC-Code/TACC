class BackupThread extends Thread {
    public final String getType() {
        String pdf_type = "";
        try {
            movePointer(0);
            pdf_type = pdf_datafile.readLine();
            int pos = pdf_type.indexOf("%PDF");
            if (pos != -1) pdf_type = pdf_type.substring(pos + 5);
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " in reading type");
        }
        return pdf_type;
    }
}
