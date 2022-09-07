class BackupThread extends Thread {
    private static void sendStop() {
        try {
            File f = new File(new File("finchconfig").getAbsolutePath() + "/serversettings.xml");
            if (!f.exists()) {
                String msg = "Can't find configuration file '" + f.getAbsolutePath() + "'.";
                System.out.println(msg);
            }
            Document doc = XMLReader.readXMLDocument(f, null, false, null);
            if (doc == null) throw new Exception("Can't read config file!");
            Element root = doc.getDocumentElement();
            Element el_settings = XMLReader.findSubElement(root, "serversettings");
            Map<String, String> settings = new HashMap<String, String>();
            XMLReader.readChildsToMap(el_settings, settings, true);
            if (!"true".equals(settings.get(SyncMain.S_ENABLESTATUSPAGE))) {
                System.out.println("Server'S status page must be enabled to stop the server from the commandline!\nServer not stopped!");
                return;
            }
            String login = settings.get(SyncMain.S_ADMINLOGIN);
            String password = settings.get(SyncMain.S_ADMINPASSWORD);
            String port = settings.get(SyncMain.S_PORTHTTP);
            String u = "http://localhost:" + port + "/status?login=" + login + "&password=" + password + "&action=stopserver";
            URL url = new URL(u);
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            hc.setUseCaches(false);
            if (hc.getResponseCode() == 200) System.out.println("Server stopped!");
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
    }
}
