class BackupThread extends Thread {
    private File createManifestationFile(final CompetitionDatas competitionDatas) throws IOException {
        final File dir = new File(OjtConstants.PERSISTANCY_DIRECTORY, FileNameComposer.composeDirectoryName(competitionDatas.getCompetitionDescriptor()));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final CompetitionDescriptor compDescr = competitionDatas.getCompetitionDescriptor();
        File manifFile = new File(dir, FileNameComposer.composeFileName(compDescr, extractExtensionFromFileName(competitionDatas.getCompetitionFile().getName())));
        if (manifFile.getName().equals(competitionDatas.getCompetitionFile().getName())) {
            manifFile = new File(dir, FileNameComposer.composeFileName(compDescr, "_1" + extractExtensionFromFileName(competitionDatas.getCompetitionFile().getName())));
        }
        FileUtils.copyFile(competitionDatas.getCompetitionFile(), manifFile);
        return manifFile;
    }
}
