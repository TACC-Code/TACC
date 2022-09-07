class BackupThread extends Thread {
        private void readOutput(OutputWriter writer, InputStream is) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String outputString = null;
                while ((outputString = reader.readLine()) != null) {
                    writer.println(outputString);
                }
            } catch (Exception ex) {
                writer.println("Could not read process output " + ex);
            }
        }
}
