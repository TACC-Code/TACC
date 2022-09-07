class BackupThread extends Thread {
    public Index(String indexDir, RemoteQueryBroker remoteQueryBroker) {
        this.remoteQueryBroker = remoteQueryBroker;
        this.indexDir = indexDir;
        File dir = new File(indexDir);
        this.indexName = dir.getName();
        if (!(dir.exists() && dir.isDirectory())) {
            try {
                Analyzer analyzer = new WhitespaceAnalyzer();
                boolean createFlag = true;
                writer = new IndexWriter(indexDir, analyzer, createFlag);
                writer.optimize();
                writer.close();
                System.out.println("Index created sucessfully");
            } catch (CorruptIndexException e) {
                e.printStackTrace();
            } catch (LockObtainFailedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                writer = new IndexWriter(indexDir, new WhitespaceAnalyzer(), false);
                System.out.println("Index already exists. It contains " + writer.docCount() + " docs");
                writer.close();
            } catch (CorruptIndexException e) {
                e.printStackTrace();
            } catch (LockObtainFailedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.updateBroker();
    }
}
