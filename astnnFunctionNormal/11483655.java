class BackupThread extends Thread {
    @Override
    public SearchResult search(String keywords, IndexHighLighterInterface ih, int page) throws LuceneException {
        List<ResultData> searchResult = null;
        long timeStart = System.currentTimeMillis();
        Hits hits = null;
        try {
            if (!reader.isCurrent()) {
                searcher.close();
                reader.close();
                reader = IndexReader.open(directory);
                searcher = new IndexSearcher(reader);
            }
            Query query = parser.parse(keywords).rewrite(reader);
            hits = searcher.search(query);
            searchResult = ih.getHighLightedList(hits, reader, query, page);
        } catch (Exception e) {
            throw new LuceneException(e);
        }
        long timeEnd = System.currentTimeMillis();
        return new DefaultSearchResult(searchResult, (timeEnd - timeStart), hits == null ? 0L : hits.length());
    }
}
