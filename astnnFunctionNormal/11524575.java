class BackupThread extends Thread {
        public void actionPerformed(ActionEvent e) {
            try {
                Model rssModel = ModelFactory.createDefaultModel();
                Resource channel = ResourceFactory.createResource(pluginUI.getChannelURI());
                rssModel.add(ResourceFactory.createStatement(channel, RDF.type, RSS.channel));
                rssModel.add(ResourceFactory.createStatement(channel, RSS.link, rssModel.createLiteral(pluginUI.getChannelLink())));
                rssModel.add(ResourceFactory.createStatement(channel, RSS.description, rssModel.createLiteral(pluginUI.getChannelDescription())));
                rssModel.add(ResourceFactory.createStatement(channel, RSS.title, rssModel.createLiteral(pluginUI.getChannelTitle())));
                rssModel.add(ResourceFactory.createStatement(channel, DC.language, rssModel.createLiteral(pluginUI.getChannelLanguage())));
                Resource anon = ResourceFactory.createResource();
                rssModel.add(ResourceFactory.createStatement(channel, RSS.items, anon));
                rssModel.add(ResourceFactory.createStatement(anon, RDF.type, RDF.Seq));
                int itemCnt = 0;
                DOMParser parser = new DOMParser();
                for (int i = 0; i < tblModel.getRowCount(); i++) {
                    Boolean bool = (Boolean) tblModel.getValueAt(i, 0);
                    if (bool.booleanValue()) {
                        URI uri = (URI) tblModel.getValueAt(i, 1);
                        try {
                            parser.parse(uri.toString());
                        } catch (SAXException saxEx) {
                            saxEx.printStackTrace();
                        } catch (IOException ioEx) {
                            ioEx.printStackTrace();
                        }
                        ItemInfo info = (ItemInfo) itemInfoMap.get(uri);
                        storeItemInfo(parser.getDocument().getDocumentElement(), info);
                        Resource item = ResourceFactory.createResource(info.getURI().toString());
                        rssModel.add(ResourceFactory.createStatement(anon, RDF.li(itemCnt++), item));
                        rssModel.add(ResourceFactory.createStatement(item, RDF.type, RSS.item));
                        rssModel.add(ResourceFactory.createStatement(item, RSS.link, rssModel.createLiteral(info.getLink())));
                        rssModel.add(ResourceFactory.createStatement(item, RSS.description, rssModel.createLiteral(info.getDescription())));
                        rssModel.add(ResourceFactory.createStatement(item, RSS.title, rssModel.createLiteral(info.getTitle())));
                    }
                }
                replaceRDFModel(rssModel);
            } catch (RDFException rdfex) {
                rdfex.printStackTrace();
            }
            frame.setVisible(false);
        }
}
