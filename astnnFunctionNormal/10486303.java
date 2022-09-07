class BackupThread extends Thread {
    private void readInReuters(Ticker ticker, String reuters, int nostories) {
        Boolean reallydone = false;
        int storycounter = 0;
        int pagenumber = 1;
        JobRunner runner = new ThreadPool(4);
        Job job = new URLExploreJob();
        while (!reallydone && storycounter < nostories) {
            try {
                String nexturl = reuters.concat("&pn=" + pagenumber);
                URL reutersurl = new URL(nexturl);
                BufferedReader reutersreader = new BufferedReader(new InputStreamReader(reutersurl.openStream()));
                System.out.println(ticker.getSymbol() + ":" + nexturl);
                Scanner s3 = new Scanner(reutersreader);
                s3.useDelimiter("[\n\r]+");
                Boolean done = false;
                while (s3.hasNext() && !done && storycounter < nostories) {
                    String next = s3.next();
                    if (next.contains("<div class=\"secondaryContent\">")) {
                        done = true;
                    }
                    if (next.contains("<div class=\"searchResult\">")) {
                        String extractor = s3.next();
                        String extractor2 = extractor.substring(extractor.indexOf("href"));
                        String title = extractor2.substring(extractor2.indexOf('>') + 1, extractor2.indexOf('<'));
                        URL exploreurl = new URL(extractor2.substring(extractor2.indexOf('\"') + 1, extractor2.indexOf('>') - 1) + "?sp=true");
                        s3.next();
                        String dateextractor = s3.next().substring(2);
                        String da = dateextractor.substring(dateextractor.indexOf('>') + 1, dateextractor.indexOf('<'));
                        Date date = dconv(da);
                        URLExploreJobInput input = new URLExploreJobInput();
                        input.exploreurl = exploreurl;
                        input.title = title;
                        input.time = date.getTime();
                        runner.addJob(job, input);
                        if (date.before(startdate)) {
                            reallydone = true;
                            break;
                        }
                        storycounter++;
                    }
                }
                pagenumber++;
                if (s3 != null) {
                    s3.close();
                }
                if (reutersreader != null) {
                    reutersreader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            List<Object> result = runner.dispatch();
            for (Object out : result) {
                URLExploreJobOutput output = (URLExploreJobOutput) out;
                ticker.addNews(new NewsEvent(output.title, output.content, output.time));
            }
        } catch (BusyException e) {
            e.printStackTrace();
        }
    }
}
