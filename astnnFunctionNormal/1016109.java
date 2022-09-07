class BackupThread extends Thread {
    private void setUp() {
        if (!(FRANCTIONAL_ODDS_FORMAT.equals(oddsFormat) || AMERICAN_ODDS_FORMAT.equals(oddsFormat) || DECIMAL_ODDS_FORMAT.equals(oddsFormat))) {
            oddsFormat = DEFAULT_ODDS_FORMAT;
        }
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieStore cookieStore = cookieManager.getCookieStore();
        for (String site : raceRegistry.getAllSites()) {
            HttpCookie cookie = new HttpCookie(ODDS_FORMAT_COOKIE_NAME, oddsFormat);
            cookie.setPath(ODDS_FORMAT_COOKIE_PATH);
            try {
                URI uri = new URI("http://" + site);
                cookieStore.add(uri, cookie);
            } catch (URISyntaxException e) {
                logger.error(e);
            }
        }
        CookieHandler.setDefault(cookieManager);
        logger.info("Odds format being used: " + oddsFormat);
        for (String site : raceRegistry.getAllSites()) {
            try {
                URL url = new URL("http://" + site + "/horse-racing/");
                URLConnection conn = url.openConnection();
                InputStream is = conn.getInputStream();
                is.close();
            } catch (MalformedURLException e) {
                logger.error(e.getLocalizedMessage(), e);
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
