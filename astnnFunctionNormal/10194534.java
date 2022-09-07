class BackupThread extends Thread {
    private static void queryZip(int zipCode, XMLReader xr) {
        URL url;
        try {
            url = new URL("http://xoap.weather.com/weather/local/" + zipCode + "?cc=*");
        } catch (Exception e) {
            System.err.println("Could not create a valid URL for zip " + zipCode);
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        URLConnection urlConn;
        try {
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(false);
            urlConn.setUseCaches(false);
        } catch (Exception e) {
            System.err.println("Could not open a connection to given url: " + url);
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        try {
            xr.parse(new InputSource(urlConn.getInputStream()));
        } catch (Exception e) {
            System.err.println("Could not parse data at given url: " + url);
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
    }
}
