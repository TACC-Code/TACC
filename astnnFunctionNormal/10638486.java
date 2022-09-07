class BackupThread extends Thread {
    public void init(BufferedReader br) throws IOException {
        m_writer = new BufferedWriter(new PipedWriter(m_reader));
        String line = br.readLine();
        write("<?xml version=\"1.0\"?>\n");
        write("<?OFX ");
        while (line.indexOf("<") != 0) {
            if (line.length() > 0) {
                write(line.replaceAll(":", "=\"") + "\" ");
            }
            line = br.readLine();
        }
        write("?>\n");
        while (line != null) {
            m_ofx += line + "\n";
            line = br.readLine();
        }
        br.close();
        new Thread(this).start();
    }
}
