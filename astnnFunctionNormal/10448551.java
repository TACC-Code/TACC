class BackupThread extends Thread {
    public void doDispatch(UMOEvent event) throws Exception {
        monitoringController.updateStatus(connector, CONNECTOR_TYPE, Event.BUSY);
        if (logger.isDebugEnabled()) {
            logger.debug("Dispatch event: " + event);
        }
        MessageObject messageObject = messageObjectController.getMessageObjectFromEvent(event);
        if (messageObject == null) {
            return;
        }
        try {
            Context context = Context.enter();
            Scriptable scope = new ImporterTopLevel(context);
            JavaScriptScopeUtil.buildScope(scope, messageObject, logger);
            scope.put("configuration", scope, ConfigurationLoader.getInstance());
            Script compiledScript = compiledScriptCache.getCompiledScript(this.connector.getScriptId());
            if (compiledScript == null) {
                logger.warn("script could not be found in cache");
                messageObjectController.setError(messageObject, Constants.ERROR_414, "Script not found in cache", null);
            } else {
                compiledScript.exec(context, scope);
                String response = "Script execution successful";
                if (messageObject.getResponseMap().containsKey(messageObject.getConnectorName())) {
                    response = (String) messageObject.getResponseMap().get(messageObject.getConnectorName());
                }
                messageObjectController.setSuccess(messageObject, response);
            }
        } catch (Exception e) {
            logger.debug("Error dispatching event: " + e.getMessage(), e);
            alertController.sendAlerts(((IheConnector) connector).getChannelId(), Constants.ERROR_414, "Error executing script", e);
            messageObjectController.setError(messageObject, Constants.ERROR_414, "Error executing script: ", e);
            connector.handleException(e);
        } finally {
            monitoringController.updateStatus(connector, CONNECTOR_TYPE, Event.DONE);
        }
    }
}
