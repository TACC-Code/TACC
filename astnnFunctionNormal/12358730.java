class BackupThread extends Thread {
    private boolean changeConnection(String service) {
        if (currentService != null) {
            handleServiceDisconnect();
        }
        EmbeddedDatabase database = EmbeddedDatabase.getSharedInstance();
        try {
            ServiceReference reference = database.getServiceForName(service);
            URL url = reference.getResourceURL();
            InputStream inputStream = null;
            try {
                inputStream = url.openStream();
                InputSource inputSource = new InputSource(inputStream);
                currentService = ServiceDigester.parseService(inputSource, IsqlToolkit.getSharedEntityResolver());
            } catch (IOException error) {
                error("", error);
            } catch (Exception error) {
                error("", error);
            }
        } catch (SQLException error) {
            error("", error);
        }
        currentService.setCommandLogger(commandHistory);
        ConnectionProfile profile = currentService.getProfile();
        Connection connection = null;
        if (requiresPrompt(currentService)) {
            Frame frameOwner = (Frame) javax.swing.SwingUtilities.getAncestorOfClass(Frame.class, centerTabbedPane);
            LoginDialog dialog = new LoginDialog(frameOwner);
            dialog.setVisible(true);
            String[] tokens = dialog.getAuthTokens();
            if (tokens == null) {
                info(messages.format("jdbcworkbench.connectioncancelled", service));
                return false;
            }
            try {
                connection = currentService.getConnection(tokens[0], tokens[1]);
            } catch (SQLException ignored) {
                return false;
            }
        } else {
            try {
                connection = currentService.getConnection();
            } catch (SQLException ignored) {
                return false;
            }
        }
        Logger newLogger = Logger.getLogger(currentService.getDriverClass());
        newLogger.addAppender(documentAppender);
        try {
            String preferredSchema = buildSchemaMenu(connection, profile);
            String preferredCatalog = buildCatalogMenu(connection, profile);
            DatabaseMetaData metaData = currentService.getConnection().getMetaData();
            info(messages.format("jdbcworkbench.connectionsucessfull", service, preferredSchema, preferredCatalog));
            String name = metaData.getDriverName();
            String version = metaData.getDriverVersion();
            info(messages.format("jdbcworkbench.driver_information", name, version));
            name = metaData.getDatabaseProductName();
            version = metaData.getDatabaseProductVersion();
            info(messages.format("jdbcworkbench.database_information", name, version));
            schemaMenu.setEnabled(true);
            catalogMenu.setEnabled(true);
            schemaModel.setSchema(preferredSchema, false);
            schemaModel.setCatalog(preferredCatalog, false);
            schemaModel.updateConnection(connection, false);
            schemaModel.reload();
        } catch (SQLException e) {
            error("xxxxxx", e);
        }
        return true;
    }
}
