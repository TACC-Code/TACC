class BackupThread extends Thread {
    private void checkForUpdate() {
        try {
            URL url = new URL("http://www.utdallas.edu/~mas073100/FourRowSolitaire/version.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine = in.readLine();
            in.close();
            if (!inputLine.contains("DOCTYPE") && !version.equals(inputLine)) {
                JOptionPane.showMessageDialog(this, "There is a newer version available, " + "click help > check for updates to get it.");
            }
        } catch (Exception ex) {
        }
    }
}
