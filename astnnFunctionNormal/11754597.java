class BackupThread extends Thread {
    public boolean isReady() throws IOException {
        os.write("isready");
        os.newLine();
        os.flush();
        String line;
        while ((line = is.readLine()) != null) {
            if (line.contains("readyok")) {
                return true;
            }
        }
        return false;
    }
}
