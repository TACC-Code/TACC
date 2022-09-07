class BackupThread extends Thread {
    public LuceneSearcher(Directory dir, String[] fields, boolean readOnly) {
        m_hitCountCache = new HashMap();
        directory = dir;
        analyzer = new StandardAnalyzer();
        this.fields = (String[]) fields.clone();
        try {
            writer = new IndexWriter(directory, analyzer, !readOnly);
            writer.close();
            writer = null;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        m_readOnly = readOnly;
        if (!readOnly) {
            setReadMode(false);
        } else {
            m_readMode = false;
            setReadMode(true);
        }
    }
}
