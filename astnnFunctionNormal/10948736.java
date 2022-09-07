class BackupThread extends Thread {
    public Query rewrite(IndexReader reader) throws IOException {
        return query.rewrite(reader);
    }
}
