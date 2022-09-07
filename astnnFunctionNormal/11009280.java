class BackupThread extends Thread {
    private void initializeReports() {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("report-definition.index"), "UTF-8"));
            final List<String> reportDefinitionResources = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    reportDefinitionResources.add(line.trim());
                }
            }
            reader.close();
            for (final String reportDefinitionResource : reportDefinitionResources) {
                final BaseReportDefinition reportDefinition = XMLIO.INSTANCE.read(getClass().getResourceAsStream(reportDefinitionResource));
                final StringWriter writer = new StringWriter();
                final Reader origReader = new InputStreamReader(getClass().getResourceAsStream(reportDefinitionResource), "UTF-8");
                final char[] buffer = new char[8192];
                int readed;
                while ((readed = origReader.read(buffer)) > 0) {
                    writer.write(buffer, 0, readed);
                }
                reader.close();
                writer.close();
                final ReportDefinitionEntity entry = new ReportDefinitionEntity(reportDefinition.getName(), reportDefinition.getName(), reportDefinition.getEntityType().toString(), reportDefinition.getDescription(), writer.toString());
                m_manager.persist(entry);
            }
        } catch (final IOException e) {
            LOG.error("Initialization error", e);
        }
    }
}
