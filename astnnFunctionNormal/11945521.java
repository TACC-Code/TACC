class BackupThread extends Thread {
    public static void expeirment(Experiment ex) throws EDITSException {
        boolean overwrite = ex.isOverwrite() != null && ex.isOverwrite();
        Source<EntailmentPair> all = null;
        Boolean v = ex.isUseMemory();
        boolean loadInMemory = v == null || v;
        if (loadInMemory) all = FilesEPSource.loadFromShell(ex.getTraining()); else all = FilesEPSource.initFromShell(ex.getTraining());
        EDITS.system().outputStream().println("Loading configuration from file " + ex.getConfiguration(), 0);
        Configurations confs = SerializationManager.loadConfigurations(ex.getConfiguration());
        ConfigurationLoader loader = new ConfigurationLoader(confs);
        Configuration conf = loader.readModule(EDITS.ENTAILMENT_ENGINE);
        EntailmentEngine engine = (EntailmentEngine) ModuleLoader.initialize(conf.getClassName());
        engine.configure(conf);
        engine.train(all);
        EDITS.system().outputStream().print(engine.describe());
        EDITS.system().outputStream().print(engine.statistics());
        String model = ex.getModel();
        if (model != null) {
            model += (ex.isAddDate() != null && ex.isAddDate() ? SerializationManager.getDate() : "");
        }
        if (model != null) {
            File f = new File(model);
            if (f.exists()) {
                if (!overwrite) throw new EDITSException("The model " + model + " already exists");
                if (f.isDirectory()) throw new EDITSException("The model is a directory!");
            }
        }
        if (model != null) SerializationManager.export(model, engine);
        if (ex.getTest().size() == 0) return;
        String output = ex.getOutput();
        List<Interlocutor<EntailmentPair, EntailmentPair>> prs = FileInterlocutor.make(ex.getTest(), output, overwrite, loadInMemory);
        Output outputType = Output.NO;
        String outputT = ex.getOutputType();
        if (output != null && outputT != null && outputT.length() > 0) {
            if (outputT.equalsIgnoreCase("simple")) outputType = Output.SIMPLE;
            if (outputT.equalsIgnoreCase("full")) outputType = Output.FULL;
            if (outputT.equalsIgnoreCase("extended")) outputType = Output.EXTENDED;
        }
        List<EvaluationStatistics> alls = new ArrayList<EvaluationStatistics>(prs.size());
        for (Interlocutor<EntailmentPair, EntailmentPair> a : prs) {
            alls.add(engine.evaluate(a, outputType));
        }
        EvaluationStatistics stats = alls.size() == 1 ? alls.get(0) : EntailmentEngineUtils.combine(alls, engine.trainedAsThree());
        EDITS.system().outputStream().print(stats);
    }
}
