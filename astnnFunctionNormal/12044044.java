class BackupThread extends Thread {
    public ArrayList<GeocodedAddress> executeRequest(OperationContext context, GeocodeParams params) throws Throwable {
        String srvCfg = context.getRequestContext().getApplicationConfiguration().getCatalogConfiguration().getParameters().getValue("openls.geocode");
        if (params.getAddresses().size() <= 0) {
            Logger.getLogger("In processRequest: No address.");
        }
        InputStream is = null;
        for (int i = 0; i < params.getAddresses().size(); i++) {
            Address reqAddr = new Address();
            reqAddr = (Address) params.getAddresses().get(i);
            String sGeocodeRequest = makeGeocodeRequest(reqAddr);
            String sUrl = srvCfg + "/findAddressCandidates?" + sGeocodeRequest;
            LOGGER.info("REQUEST=\n" + sUrl);
            URL url = new URL(sUrl);
            URLConnection conn = url.openConnection();
            String line = "";
            String sResponse = "";
            is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader rd = new BufferedReader(isr);
            while ((line = rd.readLine()) != null) {
                sResponse += line;
            }
            rd.close();
            url = null;
            return parseGeocodeResponse(sResponse);
        }
        return null;
    }
}
