class BackupThread extends Thread {
    public void getClients() {
        String line;
        clients.clear();
        try {
            URL url = new URL(whazzupfile);
            URLConnection con = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            boolean clientLine = false;
            while ((line = reader.readLine()) != null) {
                if ((clientLine) && line.startsWith(";")) clientLine = false;
                if ((clientLine) && (line.startsWith("!"))) clientLine = false;
                if (clientLine) makeClient(line);
                if (line.startsWith("!CLIENTS")) clientLine = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
