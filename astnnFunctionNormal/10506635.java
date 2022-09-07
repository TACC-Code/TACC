class BackupThread extends Thread {
    protected long loadFromSource(long afterThisTime) {
        long startTime = System.currentTimeMillis();
        QuoteDataSourceDescriptor quoteDataSourceDescriptor = (QuoteDataSourceDescriptor) dataSourceDescriptor;
        List<Quote> dataPool = dataPools.get(quoteDataSourceDescriptor.sourceSymbol);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        SimpleDateFormat sdf = new SimpleDateFormat(quoteDataSourceDescriptor.dateFormat, Locale.US);
        Date fromDate = new Date();
        Date toDate = new Date();
        if (afterThisTime == FIRST_TIME_LOAD) {
            fromDate = quoteDataSourceDescriptor.fromDate;
            toDate = quoteDataSourceDescriptor.toDate;
        } else {
            calendar.setTimeInMillis(afterThisTime);
            fromDate = calendar.getTime();
        }
        calendar.setTime(fromDate);
        int a = calendar.get(Calendar.MONTH);
        int b = calendar.get(Calendar.DAY_OF_MONTH);
        int c = calendar.get(Calendar.YEAR);
        calendar.setTime(toDate);
        int d = calendar.get(Calendar.MONTH);
        int e = calendar.get(Calendar.DAY_OF_MONTH);
        int f = calendar.get(Calendar.YEAR);
        BufferedReader dis;
        StringBuffer urlStr = new StringBuffer();
        urlStr.append("http://www.netfonds.se/quotes/paperhistory.php").append("?paper=");
        urlStr.append(quoteDataSourceDescriptor.sourceSymbol);
        try {
            URL url = new URL(urlStr.toString());
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(true);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            dis = new BufferedReader(isr);
            String s = dis.readLine();
            int iDateTime = 0;
            int iOpen = 3;
            int iHigh = 4;
            int iLow = 5;
            int iClose = 6;
            int iVolume = 7;
            int iAmount = 8;
            count = 0;
            calendar.clear();
            while ((s = dis.readLine()) != null) {
                String[] items;
                items = s.split("\t");
                if (items.length < 6) {
                    break;
                }
                Date date = null;
                try {
                    date = sdf.parse(items[iDateTime].trim());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    continue;
                }
                calendar.clear();
                calendar.setTime(date);
                long time = calendar.getTimeInMillis();
                if (time <= afterThisTime) {
                    continue;
                }
                Quote quote = new Quote();
                quote.time = time;
                quote.open = Float.parseFloat(items[iOpen].trim());
                quote.high = Float.parseFloat(items[iHigh].trim());
                quote.low = Float.parseFloat(items[iLow].trim());
                quote.close = Float.parseFloat(items[iClose].trim());
                quote.volume = Float.parseFloat(items[iVolume].trim()) / 100f;
                quote.amount = Float.parseFloat(items[iAmount].trim()) / 100f;
                if (quote.high * quote.low * quote.close == 0) {
                    quote = null;
                    continue;
                }
                dataPool.add(quote);
                if (count == 0) {
                    firstTime = time;
                }
                lastTime = time;
                setAscending((lastTime >= firstTime) ? true : false);
                count++;
            }
        } catch (Exception ex) {
            System.out.println("Error in Reading File: " + ex.getMessage());
        }
        long newestTime = (lastTime >= firstTime) ? lastTime : firstTime;
        return newestTime;
    }
}
