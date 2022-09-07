class BackupThread extends Thread {
    private void appendUserPreferences(PrintWriter writer) {
        IPreferencesService service = Platform.getPreferencesService();
        IEclipsePreferences node = service.getRootNode();
        ByteArrayOutputStream stm = new ByteArrayOutputStream();
        try {
            service.exportPreferences(node, stm, null);
        } catch (CoreException e) {
            writer.println("Error reading preferences " + e.toString());
        }
        writer.println();
        writer.println(WorkbenchMessages.SystemSummary_userPreferences);
        BufferedReader reader = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(stm.toByteArray());
            reader = new BufferedReader(new InputStreamReader(in, "8859_1"));
            char[] chars = new char[8192];
            while (true) {
                int read = reader.read(chars);
                if (read <= 0) {
                    break;
                }
                writer.write(chars, 0, read);
            }
        } catch (IOException e) {
            writer.println("Error reading preferences " + e.toString());
        }
    }
}
