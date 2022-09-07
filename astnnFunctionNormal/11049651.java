class BackupThread extends Thread {
    @Override
    public void add(genomeObject obj) {
        String readsLine = String.format("%s\t%d\t%c\t%d\t%c\t%d\n", obj.getChr(), obj.getStart(), ((cpgReads) obj).getMethyStatus(), ((cpgReads) obj).getbaseQ(), ((cpgReads) obj).getstrand(), ((cpgReads) obj).getEncryptID());
        try {
            mWriter.write(readsLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
