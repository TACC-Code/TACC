class BackupThread extends Thread {
    private Map<Channel, List<Programme>> loadChannelsFromNet() throws IOException, MalformedURLException, SAXException {
        Log.i(CLASS_NAME, "Going to parse programmes");
        HttpURLConnection uc = (HttpURLConnection) new URL(URL).openConnection();
        uc.setDoInput(true);
        uc.setDoOutput(true);
        InputStream is = uc.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, new Windows1250()), 8096 * 4);
        ProgrammeHandler handler = new ProgrammeHandler();
        long start = System.currentTimeMillis();
        Xml.parse(br, handler);
        long stop = System.currentTimeMillis();
        Log.i(CLASS_NAME, "Parsed in " + (stop - start) + " millis");
        Map<Channel, List<Programme>> channels = handler.getChannels();
        return channels;
    }
}
