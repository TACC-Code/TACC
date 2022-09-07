class BackupThread extends Thread {
    public static boolean detectSwarmcast() {
        try {
            Properties props = System.getProperties();
            props.setProperty("proxyHost", "localhost");
            props.setProperty("proxyPort", "8001");
            System.setProperties(props);
            URL url = new URL("http://proxyapi.swarmcast.net/api/js/ping.js");
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) return false;
            return line.equals("pong=true");
        } catch (Exception e) {
            System.err.println("Error contacting Swarmcast: " + e);
            return false;
        }
    }
}
