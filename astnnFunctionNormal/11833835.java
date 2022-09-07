class BackupThread extends Thread {
    public static void doExchange(URL url, File pay, File exchange) throws Exception {
        String payString = "";
        byte[] ba = new byte[1024];
        InputStream in = new FileInputStream(pay);
        PrintStream out = new PrintStream(exchange);
        while (in.read(ba) > 0) {
            String s1 = new String(ba);
            s1 = s1.trim();
            payString += s1;
            ba = new byte[1024];
        }
        in.close();
        payString = "payment=" + URLEncoder.encode(payString, "UTF-8");
        URLConnection webConnection = url.openConnection();
        webConnection.setDoOutput(true);
        OutputStreamWriter webOut = new OutputStreamWriter(webConnection.getOutputStream());
        webOut.write(payString);
        webOut.close();
        BufferedReader webIn = new BufferedReader(new InputStreamReader(webConnection.getInputStream()));
        String exchangeString;
        while ((exchangeString = webIn.readLine()) != null) {
            out.println(exchangeString);
        }
        webIn.close();
        out.close();
    }
}
