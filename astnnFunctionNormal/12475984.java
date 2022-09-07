class BackupThread extends Thread {
    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            user.setAuthToken(new AuthToken(user.getEmail(), user.getPassword()).getAuthToken());
            String fileName = "INSERT_OUTPUT_FILE_NAME_HERE";
            long reportDefintionId = Long.parseLong("INSERT_REPORT_DEFINITION_ID_HERE");
            String url = "https://adwords.google.com/api/adwords/reportdownload?__rd=" + reportDefintionId;
            HttpURLConnection urlConn = (HttpURLConnection) new URL(url).openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Authorization", "GoogleLogin auth=" + user.getRegisteredAuthToken());
            if (user.getClientCustomerId() != null) {
                urlConn.setRequestProperty("clientCustomerId", user.getClientCustomerId());
            } else if (user.getClientEmail() != null) {
                urlConn.setRequestProperty("clientEmail", user.getClientEmail());
            } else {
                urlConn.setRequestProperty("clientEmail", user.getEmail());
            }
            urlConn.connect();
            writeStreamToStream(urlConn.getInputStream(), new FileOutputStream(new File(fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
