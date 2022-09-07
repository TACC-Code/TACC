class BackupThread extends Thread {
    private static Analyzer createAnalysingAnalyzer(final Analyzer nestedAnalyzer) {
        return new Analyzer() {

            public TokenStream tokenStream(String fieldName, Reader reader) {
                try {
                    StringWriter writer = new java.io.StringWriter();
                    pipe(reader, writer);
                    String asString = writer.toString();
                    TokenStream stream = nestedAnalyzer.tokenStream(fieldName, new StringReader(asString));
                    System.out.println("Tokens for '" + asString + "':");
                    Token token;
                    while ((token = stream.next()) != null) {
                        System.out.println("  '" + token.termText() + "'");
                    }
                    return nestedAnalyzer.tokenStream(fieldName, new StringReader(asString));
                } catch (IOException exc) {
                    System.out.println("exc: " + exc);
                    return null;
                }
            }
        };
    }
}
