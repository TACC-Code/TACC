class BackupThread extends Thread {
    private com.vividsolutions.jts.geom.Geometry getGeometry(Document doc) throws Exception {
        NamespaceContext ctx = new NamespaceContext() {

            public String getNamespaceURI(String prefix) {
                String uri;
                if (prefix.equals("yahoo")) uri = "http://www.yahooapis.com/v1/base.rng"; else if (prefix.equals("ys")) uri = "http://wherein.yahooapis.com/v1/schema"; else if (prefix.equals("ys2")) uri = "http://where.yahooapis.com/v1/schema.rng"; else if (prefix.equals("gml")) uri = "http://www.opengis.net/gml"; else uri = null;
                return uri;
            }

            public Iterator getPrefixes(String val) {
                return null;
            }

            public String getPrefix(String uri) {
                return null;
            }
        };
        try {
            setRandomProxy();
            List<com.vividsolutions.jts.geom.Geometry> list = new ArrayList<com.vividsolutions.jts.geom.Geometry>();
            NodeList lst = doc.getDocumentElement().getElementsByTagName("gml:Box");
            StringWriter sw = new StringWriter();
            ByteArrayOutputStream w = new ByteArrayOutputStream();
            if (lst.getLength() != 0) return JTSAdapter.export(GMLGeometryAdapter.wrap((Element) (lst.item(0))));
            javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
            javax.xml.xpath.XPath xpath = factory.newXPath();
            xpath.setNamespaceContext(ctx);
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            dfactory.setValidating(false);
            dfactory.setNamespaceAware(true);
            DocumentBuilder loader = dfactory.newDocumentBuilder();
            printXML(doc.getDocumentElement(), new PrintStream(w));
            Document doc2 = loader.parse(new ByteArrayInputStream(w.toByteArray()));
            String name = xpath.compile("//ys:geographicScope/ys:name/text()").evaluate(doc2);
            String woeid = xpath.compile("//ys:geographicScope/ys:woeId/text()").evaluate(doc2);
            String swLat = xpath.compile("//ys:extents/ys:southWest/ys:latitude/text()").evaluate(doc2);
            String swLon = xpath.compile("//ys:extents/ys:southWest/ys:longitude/text()").evaluate(doc2);
            String neLat = xpath.compile("//ys:extents/ys:northEast/ys:latitude/text()").evaluate(doc2);
            String neLon = xpath.compile("//ys:extents/ys:northEast/ys:longitude/text()").evaluate(doc2);
            List<String> parents = new ArrayList<String>();
            if (woeid != null && woeid.trim().length() > 0 && !woeid.trim().equals("1")) {
                if (woeid.equals("2461607") || woeid.equals("55959673")) {
                    woeid = "55959673";
                    name = "North Sea";
                }
                try {
                    String url = "http://where.yahooapis.com/v1/place/" + woeid + "/belongtos;count=0?appid=" + yahooAppId;
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    doc2 = loader.parse(conn.getInputStream());
                    NodeList auxp = (NodeList) xpath.compile("//ys2:place/ys2:woeid/text()").evaluate(doc2, XPathConstants.NODESET);
                    for (int i = auxp.getLength() - 1; i >= 0; i--) parents.add(auxp.item(i).getNodeValue().trim());
                    parents.add(woeid);
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String url = "http://where.yahooapis.com/v1/place/" + woeid + "?appid=" + yahooAppId;
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    doc2 = loader.parse(conn.getInputStream());
                    swLat = xpath.compile("//ys2:boundingBox/ys2:southWest/ys2:latitude/text()").evaluate(doc2);
                    swLon = xpath.compile("//ys2:boundingBox/ys2:southWest/ys2:longitude/text()").evaluate(doc2);
                    neLat = xpath.compile("//ys2:boundingBox/ys2:northEast/ys2:latitude/text()").evaluate(doc2);
                    neLon = xpath.compile("//ys2:boundingBox/ys2:northEast/ys2:longitude/text()").evaluate(doc2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (swLat == null || swLat.length() == 0 || sw.toString().trim().length() == 0) sw.write("<gml:Box xmlns:gml='http://www.opengis.net/gml'><gml:coord><gml:X>-180</gml:X><gml:Y>-90</gml:Y></gml:coord><gml:coord><gml:X>180</gml:X><gml:Y>90</gml:Y></gml:coord></gml:Box>"); else sw.write("<gml:Box xmlns:gml='http://www.opengis.net/gml'><gml:coord><gml:X>" + swLon + "</gml:X><gml:Y>" + swLat + "</gml:Y></gml:coord><gml:coord><gml:X>" + neLon + "</gml:X><gml:Y>" + neLat + "</gml:Y></gml:coord></gml:Box>");
            Geometry geo = JTSAdapter.export(GMLGeometryAdapter.wrap(sw.toString()));
            geo.setUserData(new Pair<String, List<String>>(name, parents));
            return geo;
        } catch (Throwable e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            sw.write("<gml:Box xmlns:gml='http://www.opengis.net/gml'><gml:coord><gml:X>-180</gml:X><gml:Y>-90</gml:Y></gml:coord><gml:coord><gml:X>180</gml:X><gml:Y>90</gml:Y></gml:coord></gml:Box>");
            Geometry geo = JTSAdapter.export(GMLGeometryAdapter.wrap(sw.toString()));
            List<String> parents = new ArrayList<String>();
            parents.add("1");
            geo.setUserData(new Pair<String, List<String>>("World", parents));
            return geo;
        }
    }
}
