class BackupThread extends Thread {
    public String downloadFile() {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            try {
                final File outFile = File.createTempFile("csvdata", ".csv");
                final BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
                try {
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        writer.write(line);
                        writer.newLine();
                    }
                } finally {
                    writer.flush();
                    writer.close();
                }
                return outFile.getAbsolutePath();
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new YahooDownloadException(e);
        }
    }
}
