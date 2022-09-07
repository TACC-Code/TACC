class BackupThread extends Thread {
    private StringBuffer getSequence(String ref, EntryPoint e) throws MalformedURLException, SAXException, IOException, ParserConfigurationException, URISyntaxException {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        SequenceParser seqp = new SequenceParser();
        System.out.println(ref + "/dna?segment=" + e.id + ":" + e.start + "," + e.stop);
        parser.parse(URIFactory.url(ref + "/dna?segment=" + e.id + ":" + e.start + "," + e.stop).openStream(), seqp);
        return clean(seqp.seq);
    }
}
