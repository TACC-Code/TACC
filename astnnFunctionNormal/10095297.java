class BackupThread extends Thread {
    public void testNLM_default_01() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/NLM/nlmcatalogrecord_060101.xsd", getClass(), m_compilerErrors);
        Assert.assertEquals(0, m_compilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.DEFAULT_OPTIONS);
        for (AlignmentType alignment : Alignments) {
            Transmogrifier encoder = new Transmogrifier();
            EXIDecoder decoder = new EXIDecoder(31);
            Scanner scanner;
            InputSource inputSource;
            encoder.setAlignmentType(alignment);
            decoder.setAlignmentType(alignment);
            encoder.setEXISchema(grammarCache);
            encoder.setPreserveWhitespaces(true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            encoder.setOutputStream(baos);
            URL url = resolveSystemIdAsURL("/NLM/catplussamp2006.xml");
            inputSource = new InputSource(url.toString());
            inputSource.setByteStream(url.openStream());
            byte[] bts;
            int n_events;
            encoder.encode(inputSource);
            bts = baos.toByteArray();
            decoder.setEXISchema(grammarCache);
            decoder.setInputStream(new ByteArrayInputStream(bts));
            scanner = decoder.processHeader();
            ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
            EXIEvent exiEvent;
            n_events = 0;
            while ((exiEvent = scanner.nextEvent()) != null) {
                ++n_events;
                exiEventList.add(exiEvent);
            }
            Assert.assertEquals(50071, n_events);
            exiEvent = exiEventList.get(47024);
            Assert.assertEquals("Interdisciplinary Studies", exiEvent.getCharacters().makeString());
        }
    }
}
