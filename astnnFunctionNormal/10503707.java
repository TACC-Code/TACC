class BackupThread extends Thread {
    @Override
    public Component createComponents(String uri, Component parent, Map<String, Object> args) {
        Component comp = null;
        URL url = getClass().getClassLoader().getResource(uri);
        if (url == null) {
            throw new IllegalArgumentException("Could not find " + uri + " in class path.");
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            PageDefinition pageDef = Executions.getCurrent().getPageDefinitionDirectly(reader, "zul");
            pageDef.setExpressionFactoryClass(OGNLFactory.class);
            comp = Executions.createComponents(pageDef, parent, args);
        } catch (IOException e) {
            throw new Error("Could not open file: " + uri);
        }
        return comp;
    }
}
