class BackupThread extends Thread {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        tv.setText("Loading...");
        setContentView(tv);
        h = new Handler() {

            public void handleMessage(Message msg) {
                tv.setText((String) msg.obj);
            }
        };
        new Thread() {

            public void run() {
                try {
                    String weather = "";
                    String url = "http://www.google.com/ig/api?&weather=beijing";
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpUriRequest req = new HttpGet(url);
                    HttpResponse resp = client.execute(req);
                    HttpEntity ent = resp.getEntity();
                    InputStream stream = ent.getContent();
                    DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document d = b.parse(new InputSource(stream));
                    NodeList n = d.getElementsByTagName("forecast_conditions");
                    for (int i = 0; i < n.getLength(); i++) {
                        weather += n.item(i).getChildNodes().item(0).getAttributes().item(0).getNodeValue();
                        weather += ", ";
                        weather += (Integer.parseInt(n.item(i).getChildNodes().item(1).getAttributes().item(0).getNodeValue()) - 32) * 5 / 9;
                        weather += " ~ ";
                        weather += (Integer.parseInt(n.item(i).getChildNodes().item(2).getAttributes().item(0).getNodeValue()) - 32) * 5 / 9;
                        weather += ", ";
                        weather += n.item(i).getChildNodes().item(4).getAttributes().item(0).getNodeValue();
                        weather += "\n";
                    }
                    Message msg = h.obtainMessage(1, 1, 1, weather);
                    h.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
