class BackupThread extends Thread {
    public WeatherInfo getCurrentWeather(String zipCode) {
        String url = SCRAPE_URL + zipCode;
        WeatherInfo weather = null;
        try {
            log.info("Scraping {}", url);
            weather = this.parseWeather(new URL(url).openStream());
        } catch (MalformedURLException e) {
            log.error("URL was malformed: {}", url, e);
        } catch (IOException e) {
            log.error("IOException when retrieving weather: {}", url, e);
        }
        if (weather == null) {
            log.warn("Unable to retrieve weather using url {}" + url);
        }
        return weather;
    }
}
