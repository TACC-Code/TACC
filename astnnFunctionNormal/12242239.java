class BackupThread extends Thread {
    public void testProjectContainerStream() throws Exception {
        String name = "mcs-project.xml";
        URL url = getClass().getResource(name);
        InputStream stream = url.openStream();
        ProjectConfigurationReader projectReader = new ProjectConfigurationReader();
        String expectedLocation = url.toExternalForm();
        RuntimeProjectConfiguration configuration = projectReader.readProject(stream, expectedLocation);
        checkConfiguration(configuration, expectedLocation);
    }
}
