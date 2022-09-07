class BackupThread extends Thread {
    private void doSearch(String word) {
        try {
            URL url = new URL("http://dict.cn/ws.php?q=" + URLEncoder.encode(word, "GBK"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XMLDataHandler handler = new XMLDataHandler(word);
            parser.parse(is, handler);
            DictData data = handler.getDictInfo();
            StringBuilder sb = new StringBuilder();
            if (data.invalid) {
                sb.append("404 Not Found.");
            } else {
                sb.append(data.key).append(": ").append(data.def);
            }
            XMPPUtils.sendMessage(sb.toString().replaceAll("\n", " "), email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
