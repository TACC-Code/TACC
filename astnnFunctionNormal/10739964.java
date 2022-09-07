class BackupThread extends Thread {
    public String ask(String s) {
        String uc = s.toUpperCase();
        uc = uc.replaceAll("\\?", "");
        String query = "";
        if (uc.startsWith("WHERE IS ")) {
            query = uc.substring(9);
        } else if (uc.startsWith("WHAT IS ") || uc.startsWith("WHEN IS ")) {
            query = uc.substring(8);
        } else if (uc.startsWith("WHO IS ")) {
            query = uc.substring(7);
        } else if (uc.startsWith("WHAT DO YOU THINK ABOUT ")) {
            query = uc.substring(24);
        } else if (uc.startsWith("DO YOU KNOW ")) {
            query = uc.substring(12);
        } else return null;
        System.out.println("asking ---> " + query);
        try {
            String result = null;
            URL url = new URL("http://www.googlism.com/index.htm?ism=" + URLEncoder.encode(query, "UTF-8"));
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            connection.setDoOutput(false);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;
            int state = 0;
            ArrayList listOfAnswer = new ArrayList();
            while ((inputLine = in.readLine()) != null) {
                if (state == 0) {
                    int textPos = inputLine.indexOf("Googlism for");
                    if (textPos >= 0) {
                        int ltrPos = inputLine.indexOf("<br>", textPos + 18);
                        if (ltrPos >= 0) {
                            listOfAnswer.add(inputLine.substring(ltrPos + 4).trim());
                            state = 1;
                        }
                    }
                } else if (state == 1) {
                    int textPos = inputLine.indexOf("<tr>");
                    if (textPos >= 0) state = 2; else {
                        String l = inputLine.trim();
                        if (l.length() > 0) listOfAnswer.add(l);
                    }
                } else {
                }
            }
            in.close();
            if (listOfAnswer.size() == 0 || (listOfAnswer.size() == 1 && ((String) listOfAnswer.get(0)).indexOf("Sorry, ") >= 0)) {
                System.out.println("result ---> none!");
                return null;
            }
            result = (String) listOfAnswer.get((int) (Math.random() * listOfAnswer.size()));
            result = result.replaceAll("(&quot;|&#39;)", "'");
            result = result.replaceAll("<br>", "");
            System.out.println("result ---> " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
