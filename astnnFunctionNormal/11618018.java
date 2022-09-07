class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: request <key>");
            return;
        }
        URL url = new URL("http://localhost:9090/node/Request/" + args[0]);
        URLConnection conn = url.openConnection();
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return;
        }
        Conduit.pump(is, System.out);
    }
}
