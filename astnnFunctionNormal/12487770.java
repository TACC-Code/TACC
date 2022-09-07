class BackupThread extends Thread {
        public ArrayList parseMib(MibLoader loader, MibLoaderLog log) throws IOException, MibLoaderException {
            Asn1Parser parser;
            MibAnalyzer analyzer;
            String msg;
            if (input != null) {
            } else if (url != null) {
                input = new InputStreamReader(url.openStream());
            } else {
                input = new FileReader(file);
            }
            try {
                analyzer = new MibAnalyzer(file, loader, log);
                parser = new Asn1Parser(input, analyzer);
                parser.getTokenizer().setUseTokenList(true);
                parser.parse();
                return analyzer.getMibs();
            } catch (ParserCreationException e) {
                msg = "parser creation error in ASN.1 parser: " + e.getMessage();
                log.addInternalError(file, msg);
                throw new MibLoaderException(log);
            } catch (ParserLogException e) {
                log.addAll(file, e);
                throw new MibLoaderException(log);
            }
        }
}
