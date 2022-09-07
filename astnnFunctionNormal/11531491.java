class BackupThread extends Thread {
    private void doRefreshEarthquakes() {
        Log.d("EarthquakeService.doRefreshEarthquakes()", "Getting feed from Earthquake RSS!");
        URL url = null;
        String quakeFeed = this.getString(R.string.quake_feed);
        HttpURLConnection httpConnection = null;
        try {
            url = new URL(quakeFeed);
            URLConnection connection = url.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document dom = db.parse(in);
                Element docEle = dom.getDocumentElement();
                NodeList nl = docEle.getElementsByTagName("entry");
                if (nl != null && nl.getLength() > 0) {
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element entry = (Element) nl.item(i);
                        Element title = (Element) entry.getElementsByTagName("title").item(0);
                        Element g = (Element) entry.getElementsByTagName("georss:point").item(0);
                        Element when = (Element) entry.getElementsByTagName("updated").item(0);
                        Element link = (Element) entry.getElementsByTagName("link").item(0);
                        String details = title.getFirstChild().getNodeValue();
                        String magnitudeString = details.split(" ")[1];
                        int end = magnitudeString.length() - 1;
                        double magnitude = Double.parseDouble(magnitudeString.substring(0, end));
                        String linkString = link.getAttribute("href");
                        String point = g.getFirstChild().getNodeValue();
                        String dt = when.getFirstChild().getNodeValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                        Date qdate = new GregorianCalendar(0, 0, 0).getTime();
                        try {
                            qdate = sdf.parse(dt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String[] locations = point.split(" ");
                        Location location = new Location("dummyGPS");
                        location.setLatitude(Double.parseDouble(locations[0]));
                        location.setLongitude(Double.parseDouble(locations[1]));
                        details = details.split(",")[1].trim();
                        Quake quake = new Quake(qdate, details, location, magnitude, linkString);
                        this.addNewQuake(quake);
                    }
                }
                in.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) httpConnection.disconnect();
        }
    }
}
