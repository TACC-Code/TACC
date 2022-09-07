class BackupThread extends Thread {
    @Override
    public void process(final CompetitionDatas competitionDatas) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                exportLabel.setForeground(Color.black);
                final File sourceFile = new File(OjtConstants.SOURCE_DIRECTORY, FileNameComposer.composeFileName(competitionDatas.getCompetitionDescriptor(), competitionDatas.getCompetitionFile().getName().substring(competitionDatas.getCompetitionFile().getName().lastIndexOf('.'))));
                try {
                    FileUtils.copyFile(competitionDatas.getCompetitionFile(), sourceFile);
                    exportLabel.setText("Fichier source enregistr� dans le fichier : " + sourceFile.getAbsolutePath());
                } catch (final Exception ex) {
                    logger.error("Erreur lors de la cr�ation du fichier source : " + sourceFile, ex);
                    exportLabel.setForeground(Color.red);
                    exportLabel.setText("Erreur lors du fichier source.");
                }
                stepFinish();
            }
        });
    }
}
