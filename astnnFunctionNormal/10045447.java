class BackupThread extends Thread {
    public float getPrice(String id) {
        try {
            URL url = new URL(urlDataServer + id);
            InputStream inputStream = url.openStream();
            Reader reader = new InputStreamReader(inputStream);
            StreamTokenizer streamTokenizer = new StreamTokenizer(reader);
            streamTokenizer.nextToken();
            String outSymbol = streamTokenizer.sval;
            if (!id.equalsIgnoreCase(outSymbol)) {
                throw new RuntimeException("Incorrect ID: " + outSymbol);
            }
            streamTokenizer.nextToken();
            streamTokenizer.nextToken();
            float price = (float) streamTokenizer.nval;
            reader.close();
            System.out.println("SERVER - the following price was found: " + price);
            return price;
        } catch (Exception e) {
            System.out.println("SERVER - Error: " + e.getMessage());
            return -999.0F;
        }
    }
}
