class BackupThread extends Thread {
    public static void main(String[] args) {
        try {
            URL x_url = new URL("http://localhost:8080/tristero/servlet/tunnel?method=openTunnel");
            URLConnection x_conn = x_url.openConnection();
            InputStreamReader is_reader = new InputStreamReader(x_conn.getInputStream());
            System.out.println("Got input stream reader");
            BufferedReader reader = new BufferedReader(is_reader);
            System.out.println("Got reader");
            String line = null;
            StringBuffer x_buf = new StringBuffer();
            System.out.println("*******************RESPONSE******************************");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                x_buf.append(line).append("\n");
            }
            String address = x_buf.toString();
            String uri = address.substring("tcp:".length());
            TcpForwarder x_forwarder = new TcpForwarder();
            x_forwarder.forward(uri, "localhost:8080");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
