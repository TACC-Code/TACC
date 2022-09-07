class BackupThread extends Thread {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) throw new IllegalArgumentException("Syntax: testServer <port>");
        Socket client = accept(Integer.parseInt(args[0]));
        try {
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            out.write("You are now connected to the test server.\r\n".getBytes("latin1"));
            int x;
            while ((x = in.read()) > -1) out.write(x);
        } finally {
            System.out.println("Closing");
            client.close();
        }
    }
}
