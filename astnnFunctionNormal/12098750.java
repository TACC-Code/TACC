class BackupThread extends Thread {
    private void saveSourceAs(String fileName) throws IOException {
        if (!fileName.endsWith(".bsh")) fileName = fileName + ".bsh";
        String path = environment.toProjectFile(projectName, fileName);
        if (!fileName.equals(editorFileName)) {
            if (new File(path).exists()) {
                int option = JOptionPane.showConfirmDialog(DevKit.this, "There is alreaddy a file named " + fileName + ". Overwrite?", "Overwrite file?", JOptionPane.YES_NO_OPTION);
                if (option != JOptionPane.YES_OPTION) return;
            }
        }
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(editor.getText().getBytes());
        fos.close();
        editorFileName = fileName;
        setModified(false);
    }
}
