class BackupThread extends Thread {
    public Client() throws UnknownHostException, IOException {
        client = new Socket("localhost", 7777);
        pw = new PrintWriter(client.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        pw.write(br.readLine());
        pw.close();
        br.close();
    }
}
