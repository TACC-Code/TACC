class BackupThread extends Thread {
    private void runTransformation(int outputFormat) throws MM4UCannotOpenMediaElementsConnectionException, IOException, MM4UGeneratorException {
        IDerivator[] derivators = new IDerivator[] { new ImageSize(), new ImagesPerPage() };
        RDFURIMediaElementsConnector connector = new RDFURIMediaElementsConnector();
        connector.openConnection();
        MM4UDeserializer deSerial = new MM4UDeserializer(connector);
        URL url = new URL(TEST_DATA_FILE);
        InputStream inStream = url.openStream();
        deSerial.doDeSerialize(inStream, Constants.getValue("derivation_url"), false);
        IVariable var = deSerial;
        for (int i = 0; i < derivators.length; i++) {
            var = derivators[i].doDerivate(var);
        }
        inStream.close();
        IGenerator myGenerator = GeneratorToolkit.getFactory(outputFormat);
        IMultimediaPresentation presentation = myGenerator.doTransform(var, "TestMetadataGeneration", new SimpleUserProfile());
        presentation.store(OUTPUT_PATH);
    }
}
