class BackupThread extends Thread {
    private void train() throws EDITSException {
        String model = EDITS.system().script().option(SAVE_MODEL_OPTION);
        if (model != null) {
            model += (EDITS.system().script().option(ADD_DATE_OPTION) != null && EDITS.system().script().option(ADD_DATE_OPTION).equals("yes") ? SerializationManager.getDate() : "");
        } else model = EDITS.system().path() + "model" + SerializationManager.getDate();
        Source<EntailmentPair> all = null;
        if (useMemory()) all = FilesEPSource.loadFromShell(EDITS.system().script().input()); else all = FilesEPSource.initFromShell(EDITS.system().script().input());
        if (model != null) {
            File f = new File(model);
            if (f.exists()) {
                if (!overwrite()) throw new EDITSException("The model " + model + " already exists");
                if (f.isDirectory()) throw new EDITSException("The model is a directory!");
            }
        }
        ConfigurationLoader loader = new ConfigurationLoader();
        Configuration conf = loader.readModule(EDITS.ENTAILMENT_ENGINE);
        EntailmentEngine engine = (EntailmentEngine) ModuleLoader.initialize(conf.getClassName());
        engine.configure(conf);
        engine.train(all);
        if (model != null) SerializationManager.export(model, engine);
        EDITS.system().outputStream().println(engine.describe(), 0);
        EDITS.system().outputStream().println(engine.statistics(), 0);
    }
}
