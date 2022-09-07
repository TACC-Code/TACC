class BackupThread extends Thread {
    @Deprecated
    public static void main(String[] args) throws IOException, ObjectStreamException {
        int ai = 0;
        String encoding = null;
        String lang = null;
        if (args.length != 5) {
            usage();
        }
        while (args[ai].startsWith("-")) {
            if (args[ai].equals("-encoding")) {
                ai++;
                if (ai < args.length) {
                    encoding = args[ai];
                    ai++;
                } else {
                    usage();
                }
            } else if (args[ai].equals("-lang")) {
                ai++;
                if (ai < args.length) {
                    lang = args[ai];
                    ai++;
                } else {
                    usage();
                }
            } else {
                usage();
            }
        }
        File trainingDataFile = new File(args[ai++]);
        FileInputStream trainingDataIn = new FileInputStream(trainingDataFile);
        ObjectStream<String> lineStream = new PlainTextByLineStream(trainingDataIn.getChannel(), encoding);
        ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);
        TokenizerCrossValidator validator = new TokenizerCrossValidator(lang, false);
        validator.evaluate(sampleStream, 10);
        FMeasure result = validator.getFMeasure();
        System.out.println(result.toString());
    }
}
