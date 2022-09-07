class BackupThread extends Thread {
    public void testValueOrder_01() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema((String) null, getClass(), m_compilerErrors);
        Assert.assertEquals(0, m_compilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.DEFAULT_OPTIONS);
        for (AlignmentType alignment : Alignments) {
            for (boolean preserveWhitespaces : new boolean[] { true, false }) {
                Transmogrifier encoder = new Transmogrifier();
                EXIDecoder decoder = new EXIDecoder(31);
                Scanner scanner;
                InputSource inputSource;
                encoder.setAlignmentType(alignment);
                decoder.setAlignmentType(alignment);
                encoder.setEXISchema(grammarCache);
                encoder.setPreserveWhitespaces(preserveWhitespaces);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                encoder.setOutputStream(baos);
                URL url = resolveSystemIdAsURL("/compression/valueOrder-01.xml");
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
                Assert.assertEquals(preserveWhitespaces ? 461 : 346, n_events);
                EventType eventType;
                EventTypeList eventTypeList;
                int pos = 0;
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_SD, exiEvent.getEventVariety());
                eventType = exiEvent.getEventType();
                Assert.assertSame(exiEvent, eventType);
                Assert.assertEquals(0, eventType.getIndex());
                eventTypeList = eventType.getEventTypeList();
                Assert.assertNull(eventTypeList.getEE());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_SE, exiEvent.getEventVariety());
                Assert.assertEquals("root", exiEvent.getName());
                Assert.assertEquals("", eventType.getURI());
                if (preserveWhitespaces) {
                    exiEvent = exiEventList.get(pos++);
                    Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                    Assert.assertEquals("\n   ", exiEvent.getCharacters().makeString());
                }
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_SE, exiEvent.getEventVariety());
                Assert.assertEquals("a", exiEvent.getName());
                Assert.assertEquals("", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                Assert.assertEquals("XXX", exiEvent.getCharacters().makeString());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_EE, exiEvent.getEventVariety());
                if (preserveWhitespaces) {
                    exiEvent = exiEventList.get(pos++);
                    Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                    Assert.assertEquals("\n   ", exiEvent.getCharacters().makeString());
                }
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_SE, exiEvent.getEventVariety());
                Assert.assertEquals("b", exiEvent.getName());
                Assert.assertEquals("", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                Assert.assertEquals("bla", exiEvent.getCharacters().makeString());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_EE, exiEvent.getEventVariety());
                if (preserveWhitespaces) {
                    exiEvent = exiEventList.get(pos++);
                    Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                    Assert.assertEquals("\n   ", exiEvent.getCharacters().makeString());
                }
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_SE, exiEvent.getEventVariety());
                Assert.assertEquals("c", exiEvent.getName());
                Assert.assertEquals("", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                Assert.assertEquals("foo", exiEvent.getCharacters().makeString());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_EE, exiEvent.getEventVariety());
                if (preserveWhitespaces) {
                    exiEvent = exiEventList.get(pos++);
                    Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                    Assert.assertEquals("\n   ", exiEvent.getCharacters().makeString());
                }
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_SE, exiEvent.getEventVariety());
                Assert.assertEquals("b", exiEvent.getName());
                Assert.assertEquals("", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                Assert.assertEquals("XXX", exiEvent.getCharacters().makeString());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_EE, exiEvent.getEventVariety());
                for (int i = 0; i < 110; i++) {
                    if (preserveWhitespaces) {
                        exiEvent = exiEventList.get(pos++);
                        Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                        Assert.assertEquals("\n   ", exiEvent.getCharacters().makeString());
                    }
                    exiEvent = exiEventList.get(pos++);
                    Assert.assertEquals(EXIEvent.EVENT_SE, exiEvent.getEventVariety());
                    Assert.assertEquals("a", exiEvent.getName());
                    Assert.assertEquals("", exiEvent.getURI());
                    exiEvent = exiEventList.get(pos++);
                    Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                    Assert.assertEquals(Integer.toString(i + 1), exiEvent.getCharacters().makeString());
                    exiEvent = exiEventList.get(pos++);
                    Assert.assertEquals(EXIEvent.EVENT_EE, exiEvent.getEventVariety());
                }
                if (preserveWhitespaces) {
                    exiEvent = exiEventList.get(pos++);
                    Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());
                    Assert.assertEquals("\n", exiEvent.getCharacters().makeString());
                }
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_EE, exiEvent.getEventVariety());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_ED, exiEvent.getEventVariety());
            }
        }
    }
}
