class BackupThread extends Thread {
    public void run() {
        String info = "";
        try {
            String data = URLEncoder.encode("queryinput", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            URL url = new URL("http://ws.arin.net/whois");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            int i = 1;
            while ((line = rd.readLine()) != null) {
                if (i > 49) {
                    if (!line.equals("")) info += line + "%";
                }
                i++;
            }
            info = Patterns.removeTags(info);
            StringTokenizer st = new StringTokenizer(info, "%");
            attributes = "";
            info = "";
            boolean alternador = false;
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                token = token.trim();
                if (token.length() > 0 && !token.substring(0, 1).equals("#")) {
                    if (token.indexOf(":") != -1) attributes += token + "\n";
                }
            }
            ASWhois as = new ASWhois(id);
            as.setAttributes(attributes);
            whoisView.setAS(as);
            whoisView.printInfo();
            wr.close();
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
