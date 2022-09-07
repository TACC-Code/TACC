class BackupThread extends Thread {
    public OntologyPersistence(String readerClassName, String writerClassName) {
        this.readerClassName = readerClassName;
        this.writerClassName = writerClassName;
    }
}
