class BackupThread extends Thread {
    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        if (call1 == null) {
            handleException(context, new ActionStepException("No current call found"));
            return;
        } else if (!(call1 instanceof Call)) {
            handleException(context, new ActionStepException("Call isn't isn't an Asterisk call: " + call1.getClass().getName()));
            return;
        }
        if (((Call) call1).getChannel() == null) {
            handleException(context, new ActionStepException("No channel found in current context"));
            return;
        }
        AgiChannel channel = ((Call) call1).getChannel();
        try {
            String astVar = variable;
            if (StringUtils.isNotBlank(astVar)) {
                if (!astVar.startsWith("${")) {
                    astVar = "${" + astVar;
                }
                if (!astVar.endsWith("}")) astVar += "}";
            }
            String value = channel.getFullVariable(astVar);
            Variable v = resolveVariableFromName(assignToVar, context);
            if (debugLog.isLoggable(Level.FINEST)) {
                debug("Got value " + value + " of variable " + astVar + ". Assigning to " + (v == null ? "null" : v.getName()));
            }
            if (v.getScope() != VariableScope.GLOBAL) context.setVariableRawValue(v.getName(), VariableTranslator.translateValue(v.getType(), value)); else {
                SafletEnvironment env = getSaflet().getSafletEnvironment();
                env.setGlobalVariableValue(v.getName(), VariableTranslator.translateValue(v.getType(), value));
            }
        } catch (Exception e) {
            handleException(context, e);
            return;
        }
        handleSuccess(context);
    }
}
