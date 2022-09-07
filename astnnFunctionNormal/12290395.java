class BackupThread extends Thread {
    public void getServers() {
        String line;
        servers.clear();
        try {
            URL url = new URL(whazzupfile);
            URLConnection con = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            boolean serverLine = false;
            while ((line = reader.readLine()) != null) {
                if ((serverLine) && line.startsWith(";")) serverLine = false;
                if ((serverLine) && (line.startsWith("!"))) serverLine = false;
                if (serverLine) makeServer(line);
                if (line.startsWith("!SERVERS")) serverLine = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
