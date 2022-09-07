class BackupThread extends Thread {
    private void prepareSettings(File settingsFile, File templateSettings, File currentSettings) throws TransformerException, IOException {
        final URL url = getClass().getResource("/settings-AddMirrors.xsl");
        final InputStream stream = url.openStream();
        try {
            final Transformer t = TRANSFORMER_FACTORY.newTransformer(new StreamSource(stream));
            t.setParameter("current.settings.xml", currentSettings.getAbsolutePath());
            t.setParameter("tmp.repo.base", tmpbase);
            settingsFile.getParentFile().mkdirs();
            t.transform(new StreamSource(templateSettings), new StreamResult(settingsFile));
        } finally {
            stream.close();
        }
    }
}
