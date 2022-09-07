class BackupThread extends Thread {
    public void testMethod() throws Exception {
        RAMDirectory directory = new RAMDirectory();
        String[] categories = new String[] { "food", "foodanddrink", "foodanddrinkandgoodtimes", "food and drink" };
        Query rw1 = null;
        Query rw2 = null;
        IndexReader reader = null;
        try {
            IndexWriter writer = new IndexWriter(directory, new WhitespaceAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
            for (int i = 0; i < categories.length; i++) {
                Document doc = new Document();
                doc.add(new Field("category", categories[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
                writer.addDocument(doc);
            }
            writer.close();
            reader = IndexReader.open(directory, true);
            PrefixQuery query = new PrefixQuery(new Term("category", "foo"));
            rw1 = query.rewrite(reader);
            BooleanQuery bq = new BooleanQuery();
            bq.add(query, BooleanClause.Occur.MUST);
            rw2 = bq.rewrite(reader);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertEquals("Number of Clauses Mismatch", getCount(reader, rw1), getCount(reader, rw2));
    }
}
