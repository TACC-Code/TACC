class BackupThread extends Thread {
    @Override
    public boolean convertir(GestionnaireErreur gest) {
        tempsExecution = System.currentTimeMillis();
        boolean retour;
        gest.afficheMessage("** Conversion en XHTML avec Writer2XHTML...", Nat.LOG_VERBEUX);
        Converter converter = ConverterFactory.createConverter(MIMETypes.XHTML_MATHML);
        Config config = converter.getConfig();
        try {
            config.read(new FileInputStream("writer2latex/xhtml/config/cleanxhtml.xml"));
            config.setOption("inputencoding", "utf-8");
            config.setOption("use_named_entities", "true");
            String t = Transcription.fTempXHTML;
            ConverterResult result = converter.convert(new FileInputStream(source), t.substring(t.lastIndexOf("/") + 1));
            result.write(new File(t.substring(0, t.lastIndexOf("/"))));
        } catch (Exception e) {
            gest.afficheMessage("Problème lors de la conversion avec Writer2XHTML " + e.getLocalizedMessage(), Nat.LOG_SILENCIEUX);
            e.printStackTrace();
            e.printStackTrace();
        }
        ConvertisseurXML convXML = new ConvertisseurXML(Transcription.fTempXHTML, cible);
        retour = convXML.convertir(gest);
        tempsExecution = System.currentTimeMillis() - tempsExecution;
        return retour;
    }
}
