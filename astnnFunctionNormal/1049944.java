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
            Date date = (Date) VariableTranslator.translateValue(VariableType.DATETIME, resolveDynamicValue(dateTime, context));
            if (debugLog.isLoggable(Level.FINEST)) debug("The date is " + date);
            char d = channel.sayDateTime(date.getTime() / 1000, escapeDigits, format, timezone);
            if (d != 0) {
                String digitPressed = String.valueOf(d);
                ((AsteriskSafletContext) context).appendBufferedDigits(digitPressed);
            }
        } catch (Exception e) {
            handleException(context, e);
            return;
        }
        handleSuccess(context);
    }
}
