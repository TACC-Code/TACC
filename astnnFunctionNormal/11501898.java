class BackupThread extends Thread {
    public static String sendRequest(String myurl, String data) {
        StringBuffer answer = new StringBuffer();
        try {
            URL url = new URL(myurl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            if (data != "") {
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                writer.close();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            reader.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return answer.toString();
    }
}
