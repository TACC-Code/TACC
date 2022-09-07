class BackupThread extends Thread {
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://radis.sf.net/update.txt");
        InputStreamReader isr = new InputStreamReader(url.openStream());
        BufferedReader br = new BufferedReader(isr);
        while (br.ready()) {
            System.out.println(br.readLine());
        }
    }
}
