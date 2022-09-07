class BackupThread extends Thread {
    @Override
    protected void initializeGraphicalViewer() {
        try {
            LoginDialog loginDialog = new LoginDialog(getGraphicalViewer().getControl().getShell(), Configuration.getInstance());
            loginDialog.setBlockOnOpen(true);
            loginDialog.open();
            if (loginDialog.getReturnCode() == Window.OK) {
                Configuration config = Configuration.getInstance();
                ModelConfiguration modelConfig = config.getModelConfiguration();
                cimClient = new SblimWbemClient(modelConfig.getCimNamespace(), config.getLocaleName());
                cimClient.connect(config.getServerUrl(), modelConfig.getCimUsername(), modelConfig.getCimPassword());
                model = new GefSchema(modelConfig, this, cimClient, getGraphicalViewer().getControl().getDisplay());
                model.enumerateRootInstances();
                getGraphicalViewer().setContents(model);
                model.setMode(GefSchema.Mode.AFTER_ENUMERATE);
                cimClient.subscribeToIndications(model);
            } else {
                PlatformUI.getWorkbench().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
