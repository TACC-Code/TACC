class BackupThread extends Thread {
    static void lookup(String s1, String s2) throws MalformedURLException, IOException, REException {
        s1 = s1.replace(' ', '+');
        s1 = s1.replace('/', '+');
        s2 = s2.replace(' ', '+');
        URL url = new URL("http://www.google.com/search?q=unit+" + s1 + "+" + s2 + "&hl=en");
        System.out.println("Trying to connect to " + url);
        URLConnection connection = url.openConnection();
        InputStreamReader in1 = new InputStreamReader(connection.getInputStream());
        BufferedReader in = new BufferedReader(in1);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
        System.out.println("\n\n\n");
    }
}
