class BackupThread extends Thread {
    private void configureConstantsPropertiesAndFunctions(final ClassConfiguration config, final ScriptableObject scriptable) {
        configureConstants(config, scriptable);
        for (final String propertyName : config.propertyKeys()) {
            final Method readMethod = config.getPropertyReadMethod(propertyName);
            final Method writeMethod = config.getPropertyWriteMethod(propertyName);
            scriptable.defineProperty(propertyName, null, readMethod, writeMethod, ScriptableObject.EMPTY);
        }
        int attributes = ScriptableObject.EMPTY;
        if (webClient_.getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_147)) {
            attributes = ScriptableObject.DONTENUM;
        }
        for (final String functionName : config.functionKeys()) {
            final Method method = config.getFunctionMethod(functionName);
            final FunctionObject functionObject = new FunctionObject(functionName, method, scriptable);
            scriptable.defineProperty(functionName, functionObject, attributes);
        }
    }
}
