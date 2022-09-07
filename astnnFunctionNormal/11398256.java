class BackupThread extends Thread {
    private void createProjectModelRepository() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return "*" + ProjectModelRepositoryStore.FILE_EXTENSION + " (mview project repositories)";
            }

            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(ProjectModelRepositoryStore.FILE_EXTENSION);
            }
        });
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int state = fileChooser.showDialog(context.getMView(), "new project repository");
        if (state == JFileChooser.APPROVE_OPTION) {
            File projectModelRepositoryFile = fileChooser.getSelectedFile();
            if (projectModelRepositoryFile.exists()) {
                Object[] options = new String[] { "overwrite", "load", "cancel" };
                int n = JOptionPane.showOptionDialog(context.getMView(), "Load or overwrite file [" + projectModelRepositoryFile.getAbsolutePath() + "]", "file [" + projectModelRepositoryFile.getAbsolutePath() + "] " + "already exists", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 2) {
                    return;
                }
                if (n == 0) {
                    ProjectModelRepository projectModelRepository = ProjectModelRepository.createDefaultProjectModelRepository();
                    projectModelRepository.setFile(projectModelRepositoryFile);
                    ProjectModelRepositoryStore.store(projectModelRepository);
                }
                if (model.updateProjectModelRepository(projectModelRepositoryFile)) {
                    updateView();
                } else {
                    LOGGER.error("failure updating project model repository");
                }
            } else {
                if (false == projectModelRepositoryFile.getName().endsWith(ProjectModelRepositoryStore.FILE_EXTENSION)) {
                    projectModelRepositoryFile = new File(projectModelRepositoryFile.getAbsolutePath() + ProjectModelRepositoryStore.FILE_EXTENSION);
                }
                ProjectModelRepository projectModelRepository = ProjectModelRepository.createDefaultProjectModelRepository();
                projectModelRepository.setFile(projectModelRepositoryFile);
                ProjectModelRepositoryStore.store(projectModelRepository);
                if (model.updateProjectModelRepository(projectModelRepositoryFile)) {
                    updateView();
                } else {
                    LOGGER.error("failure updating project model repository");
                }
            }
        }
    }
}
