class BackupThread extends Thread {
    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlString2UTF8(urlStr));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(), "gb2312");
            String string = inputStreamReader.getEncoding();
            bufferedReader = new BufferedReader(inputStreamReader);
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return sb.toString();
    }
}
