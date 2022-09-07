class BackupThread extends Thread {
    protected double numResultsFromWeb(String term) throws JSONException, IOException {
        double result = 0;
        if (cache.containsKey(term)) {
            result = cache.get(term);
        } else {
            URL url = null;
            InputStream stream = null;
            try {
                url = makeQueryURL(term);
                URLConnection connection = url.openConnection();
                stream = connection.getInputStream();
                InputStreamReader inputReader = new InputStreamReader(stream);
                BufferedReader bufferedReader = new BufferedReader(inputReader);
                double count = getCountFromQuery(bufferedReader);
                cache.put(term, count);
                newCache.put(term, count);
                updateCache(CACHE_FILE_NAME);
                result = count;
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        logger.warning(e.toString());
                    }
                }
            }
        }
        return result;
    }
}
