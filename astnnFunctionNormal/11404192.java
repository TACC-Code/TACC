class BackupThread extends Thread {
    @SuppressWarnings("unused")
    public String Parse(String _isbn, String _action) {
        Log.d("snakoid", "start parsing for isbn :" + _isbn);
        action = _action;
        try {
            action = _action;
            url = new URL(Tools.GetRequestUrl(_isbn));
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            dom = db.parse(url.openConnection().getInputStream());
            root = dom.getDocumentElement();
            NodeList nodeList1, nodeList2, nodeList3, tempsdfg;
            Node node1, node2, node3;
            Book b;
            books = new ArrayList<Book>();
            tempsdfg = root.getElementsByTagName("Error");
            if (root.getElementsByTagName("Error").getLength() != 0) {
                Log.d("snakoid", "parsing has returned an error :");
                nodeList1 = root.getElementsByTagName("Error");
                for (int i = 0; i < nodeList1.getLength(); i++) {
                    node1 = nodeList1.item(i);
                    nodeList2 = node1.getChildNodes();
                    for (int j = 0; j < nodeList2.getLength(); j++) {
                        node2 = nodeList2.item(j);
                        if (node2 != null && node2.getNodeName().equals("Code")) Log.d("snakoid", "[" + node2.getChildNodes().item(0).getNodeValue() + "]");
                        return "error : " + node2.getChildNodes().item(0).getNodeValue();
                    }
                }
            } else if (root.getElementsByTagName("Items").getLength() != 0) {
                nodeList1 = root.getElementsByTagName("Item");
                for (int i = 0; i < nodeList1.getLength(); i++) {
                    b = new Book();
                    node1 = nodeList1.item(i);
                    nodeList2 = node1.getChildNodes();
                    for (int j = 0; j < nodeList2.getLength(); j++) {
                        node2 = nodeList2.item(j);
                        if (node2 != null && node2.getNodeName().equals("DetailPageURL")) {
                            Node temp = node2.getChildNodes().item(0);
                            String temp2 = temp.getNodeValue();
                            b.setUrl(ValueOrNull(temp2));
                        } else if (node2 != null && node2.getNodeName().equals("SmallImage")) {
                            nodeList3 = node2.getChildNodes();
                            for (int m = 0; m < nodeList3.getLength(); m++) {
                                node3 = nodeList3.item(m);
                                if (node3 != null && node3.getNodeName().equals("URL")) {
                                    b.setImageUrl(ValueOrNull(node3.getChildNodes().item(0).getNodeValue()));
                                    if (b.getImageUrl() != null) b.setImageUrl(Tools.DownloadFile(b.getImageUrl(), Tools.folderThumbsPath));
                                } else if (node3 != null && node3.getNodeName().equals("Width")) b.setImageWidth(ValueOrNull(node3.getChildNodes().item(0).getNodeValue())); else if (node3 != null && node3.getNodeName().equals("Height")) b.setImageHeight(ValueOrNull(node3.getChildNodes().item(0).getNodeValue()));
                            }
                        } else if (node2 != null && node2.getNodeName().equals("MediumImage")) {
                            nodeList3 = node2.getChildNodes();
                            for (int m = 0; m < nodeList3.getLength(); m++) {
                                node3 = nodeList3.item(m);
                                if (node3 != null && node3.getNodeName().equals("URL")) {
                                    b.setMediumImageUrl(ValueOrNull(node3.getChildNodes().item(0).getNodeValue()));
                                    if (b.getMediumImageUrl() != null) b.setMediumImageUrl(Tools.DownloadFile(b.getMediumImageUrl(), Tools.folderThumbsPath));
                                }
                            }
                        } else if (node2 != null && node2.getNodeName().equals("ItemAttributes")) {
                            nodeList3 = node2.getChildNodes();
                            for (int m = 0; m < nodeList3.getLength(); m++) {
                                node3 = nodeList3.item(m);
                                if (node3 != null && node3.getNodeName().equals("Title")) b.setTitle(ValueOrNull(node3.getChildNodes().item(0).getNodeValue())); else if (node3 != null && node3.getNodeName().equals("Publisher")) b.setPublisher(ValueOrNull(node3.getChildNodes().item(0).getNodeValue())); else if (node3 != null && node3.getNodeName().equals("Studio")) b.setStudio(ValueOrNull(node3.getChildNodes().item(0).getNodeValue())); else if (node3 != null && node3.getNodeName().equals("PublicationDate")) b.setPubDate(ValueOrNull(node3.getChildNodes().item(0).getNodeValue())); else if (node3 != null && node3.getNodeName().equals("NumberOfPages")) b.setPageCount(ValueOrNull(node3.getChildNodes().item(0).getNodeValue())); else if (node3 != null && (node3.getNodeName().equals("Author") || node3.getNodeName().equals("Creator"))) b.addAuthor(ValueOrNull(node3.getChildNodes().item(0).getNodeValue())); else if (node3 != null && node3.getNodeName().equals("EAN")) b.setISBN(ValueOrNull(node3.getChildNodes().item(0).getNodeValue()));
                            }
                        }
                    }
                    books.add(b);
                }
                if (action.equals("request")) Tools.AddBooks2Temp(books);
                Log.d("snakoid", "finish parsing");
                return "parsed";
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Log.d("snakoid", "parsing error : ParserConfigurationException");
        } catch (SAXException e) {
            e.printStackTrace();
            Log.d("snakoid", "parsing error : SAXException");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "nothing done";
    }
}
