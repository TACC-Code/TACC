class BackupThread extends Thread {
    private boolean saveXml() {
        saveComponentSettings();
        String saveFileName = fileName;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                int ret = JOptionPane.showConfirmDialog(this, "The file exists already, do you want to overwrite the file?", "Save and overwrite", JOptionPane.OK_CANCEL_OPTION);
                if (ret == JOptionPane.CANCEL_OPTION) return false;
            }
            FileWriter fw = new FileWriter(fileName);
            Writer writer = new BufferedWriter(fw);
            writer.write(generateXml());
            writer.flush();
            fw.flush();
            writer.close();
            fw.close();
            EditorRegisteredComponentFactory.addConfigFile("User", fileName, true);
            EditorRegisteredComponentFactory.updateConfig();
            return true;
        } catch (IOException ex) {
            if (BuildProperties.DEBUG) DebugLogger.logError("Unable to write the project file");
        }
        return false;
    }
}
