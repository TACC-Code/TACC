class BackupThread extends Thread {
    public static String getTextFile(String TextFileURL) {
        String text_out = "";
        try {
            URL url = new URL(TextFileURL);
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setDoInput(true);
            urlConnect.setUseCaches(false);
            DataInputStream dis;
            String s;
            dis = new DataInputStream(urlConnect.getInputStream());
            while ((s = dis.readLine()) != null) {
                if (s.length() == 0) continue;
                text_out = text_out.concat(s + "\n");
            }
            dis.close();
        } catch (UnknownHostException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
        return text_out;
    }
}
