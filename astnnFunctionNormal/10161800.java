class BackupThread extends Thread {
    public MJLoader(String adr) {
        try {
            URL url = new URL(adr);
            String ln;
            BufferedReader lnr = new BufferedReader(new InputStreamReader(url.openStream()));
            while (true) {
                ln = lnr.readLine();
                if (ln == null) break;
                ln = ln.trim();
                if (!ln.substring(0, 1).equals("#")) file.add(ln);
            }
            lnr.close();
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Read error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Read error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
