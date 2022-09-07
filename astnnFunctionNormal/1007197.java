class BackupThread extends Thread {
    public static int getCode(String url_address) {
        int code = -1;
        try {
            URL url = new URL(url_address);
            HttpURLConnection cn = (HttpURLConnection) url.openConnection();
            cn.connect();
            code = cn.getResponseCode();
            System.out.println(url_address + "[" + code + "]");
            cn.disconnect();
        } catch (MalformedURLException e) {
            System.out.println("httpConnetionCode:Error[" + e + "]");
            return code;
        } catch (Exception e) {
            System.out.println("httpConnetionCode:Error[" + e + "]");
            return code;
        }
        return code;
    }
}
