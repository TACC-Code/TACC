class BackupThread extends Thread {
    @Override
    public List<WebSite> getWebSitesClosedMostOfTheTime() throws Exception {
        Set<WebSite> sites = new TreeSet<WebSite>();
        URL url = new URL(BTRACS_URL);
        InputStream is = url.openStream();
        try {
            String content = IOUtils.toString(is, "ISO-8859-1");
            String tableContent = StringUtils.substringBetween(content, "<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" class=\"TableStyle1\" style=\"width: 600px;\">", "</table>");
            String[] names = StringUtils.substringsBetween(tableContent, "onmouseout=\"this.style.textDecoration='none';\">", "</b>");
            log.fine("Names: " + Arrays.toString(names));
            String[] links = StringUtils.substringsBetween(tableContent, "<a href=\"", "\"");
            log.fine("Links: " + Arrays.toString(links));
            String[] languages = StringUtils.substringsBetween(tableContent, "style=\"border: none; width: 20px; height: 13px; position: relative; top: 2px;\">&nbsp;", "&nbsp;");
            log.fine("Languages: " + Arrays.toString(languages));
            Pattern patternCategories = Pattern.compile("<td>[a-zA-Z]*</td>");
            Matcher matcherCategories = patternCategories.matcher(tableContent);
            List<String> categories = new ArrayList<String>();
            while (matcherCategories.find()) {
                categories.add(StringUtils.substringBetween(matcherCategories.group(), "<td>", "</td>"));
            }
            log.fine("Categories: " + categories);
            Pattern patternRankings = Pattern.compile("<td>[0-9,]*</td>");
            Matcher matcherRankings = patternRankings.matcher(tableContent);
            List<String> rankings = new ArrayList<String>();
            while (matcherRankings.find()) {
                rankings.add(StringUtils.substringBetween(matcherRankings.group(), "<td>", "</td>"));
            }
            log.fine("Rankings: " + rankings);
            for (int i = 0; i < names.length; i++) {
                WebSite.Builder builder = new WebSite.Builder(names[i], links[i + 4]);
                builder.category(categories.get(i + 2));
                builder.language(languages[i].trim());
                builder.ranking(NumberUtils.createInteger(StringUtils.remove(rankings.get(i + 2), ',')));
                sites.add(builder.build());
            }
            return new ArrayList<WebSite>(sites);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
