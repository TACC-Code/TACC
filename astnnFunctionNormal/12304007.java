class BackupThread extends Thread {
    @Override
    protected void readHelpFiles() {
        File helpFile = new File(helpLocation);
        if (helpFile.exists()) {
            term.writeTo("\nLoading adventure help...");
            try {
                FileReader reader = new FileReader(helpFile);
                BufferedReader in = new BufferedReader(reader);
                String line;
                StringTokenizer lntkns;
                HelpEntry entry = null;
                while ((line = in.readLine()) != null) {
                    if (line.length() == 0) {
                        entry = readLine(entry);
                    } else {
                        lntkns = new StringTokenizer(line);
                        if (lntkns.hasMoreTokens()) {
                            entry = readLine(lntkns, entry);
                        }
                    }
                }
                entries.add(entry);
                term.writeTo("\nFinished reading " + entries.size() + " entries.");
                helpRead = true;
                reader.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            term.writeTo("\nERROR: Adventure help file not found.");
        }
    }
}
