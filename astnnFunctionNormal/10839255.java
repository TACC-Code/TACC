class BackupThread extends Thread {
    public static void post(String action, String formData) throws Exception {
        URL url = new URL(action);
        SocketAddress addr = new InetSocketAddress("210.51.14.197", 80);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        URLConnection conn = url.openConnection(proxy);
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", "210.51.14.197");
        System.getProperties().put("proxyPort", "80");
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(formData);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        wr.close();
        rd.close();
    }
}
