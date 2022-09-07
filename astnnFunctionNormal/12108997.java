class BackupThread extends Thread {
    private void findStartOfTypeMapping(BufferedReader reader, PrintWriter writer) throws IOException {
        String line = null;
        do {
            line = reader.readLine();
            if (!line.equals(START_OF_TYPE_MAPPING)) {
                writer.println(line);
            }
        } while (!line.equals(START_OF_TYPE_MAPPING));
    }
}
