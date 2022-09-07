class BackupThread extends Thread {
            public void run() {
                try {
                    EFetchPane.this.progressBar.setIndeterminate(true);
                    String term = queryField.getText().trim();
                    if (term.length() == 0) return;
                    URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed" + "&term=" + URLEncoder.encode(term, "UTF-8") + "&tool=pubmed2wikipedia" + "&email=plindenbaum_at_yahoo.fr" + "&retmode=xml&usehistory=y&retmax=" + maxReturn + "&retstart=" + retstart);
                    Debug.debug(url);
                    InputStream in = url.openStream();
                    XMLInputFactory factory = XMLInputFactory.newInstance();
                    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
                    factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
                    factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
                    XMLEventReader parser = factory.createXMLEventReader(in);
                    String QueryKey = null;
                    String WebEnv = null;
                    int idCount = 0;
                    while (parser.hasNext()) {
                        XMLEvent event = parser.nextEvent();
                        if (event.isStartElement()) {
                            StartElement element = event.asStartElement();
                            if (element.getName().getLocalPart().equals("QueryKey")) {
                                QueryKey = parser.getElementText().trim();
                            } else if (element.getName().getLocalPart().equals("WebEnv")) {
                                WebEnv = parser.getElementText().trim();
                            } else if (element.getName().getLocalPart().equals("Id")) {
                                ++idCount;
                            }
                        }
                    }
                    in.close();
                    if (QueryKey == null || WebEnv == null) {
                        throw new IOException("Cannot find QueryKey or WebEnv in " + url);
                    }
                    url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed" + "&rettype=full" + "&tool=pubmed2wikipedia" + "&email=plindenbaum_at_yahoo.fr" + "&retmode=xml" + "&WebEnv=" + WebEnv + "&query_key=" + QueryKey + "&retmode=xml&usehistory=y&retmax=" + maxReturn + "&retstart=" + retstart);
                    Debug.debug(url);
                    if (idCount > 0) {
                        in = url.openStream();
                        parser = factory.createXMLEventReader(new IgnoreLine2(new InputStreamReader(in)));
                        while (parser.hasNext()) {
                            XMLEvent event = parser.nextEvent();
                            if (event.isStartElement()) {
                                StartElement element = event.asStartElement();
                                if (element.getName().getLocalPart().equals("PubmedArticle")) {
                                    Paper p = new Paper(parser, element);
                                    papers.addElement(p);
                                }
                            }
                        }
                        in.close();
                    }
                    if (EFetchPane.this.theTread == this) {
                        SwingUtilities.invokeAndWait(new Runnable() {

                            @Override
                            public void run() {
                                EFetchPane.this.setPapers(Search.this.papers);
                                EFetchPane.this.progressBar.setIndeterminate(false);
                            }

                            ;
                        });
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                    EFetchPane.this.progressBar.setIndeterminate(false);
                    ThrowablePane.show(EFetchPane.this, err);
                    Thread.currentThread().interrupt();
                }
            }
}
