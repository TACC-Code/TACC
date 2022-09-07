class BackupThread extends Thread {
    private Path createIndex(String name, boolean hashDup, float inc, long time, boolean incFirst) throws Exception {
        Path idx = new Path(root, name);
        Path sub = new Path(idx, "part-0000");
        Directory dir = FSDirectory.getDirectory(sub.toString());
        IndexWriter writer = new IndexWriter(dir, new NutchDocumentAnalyzer(conf), true);
        Document doc = makeDoc(name, MD5Hash.digest("1").toString(), "http://www.example.com/1", 1.0f + (incFirst ? inc : 0.0f), time);
        writer.addDocument(doc);
        if (hashDup) {
            doc = makeDoc(name, MD5Hash.digest("1").toString(), "http://www.example.com/2", 1.0f + (!incFirst ? inc : 0.0f), time + 1);
        } else {
            doc = makeDoc(name, MD5Hash.digest("2").toString(), "http://www.example.com/1", 1.0f + (!incFirst ? inc : 0.0f), time + 1);
        }
        writer.addDocument(doc);
        writer.close();
        return idx;
    }
}
