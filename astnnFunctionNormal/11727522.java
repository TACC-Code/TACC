class BackupThread extends Thread {
    private double getGooglePageRank(DomainDetail dd) {
        String domain = dd.getDomainName();
        double result = -1;
        JenkinsHash jHash = new JenkinsHash();
        String googlePrResult = "";
        long hash = jHash.hash(("info:" + domain).getBytes());
        String url = "http://" + GOOGLE_PR_DATACENTER_IPS[dataCenterIdx] + "/search?client=navclient-auto&hl=en&" + "ch=6" + hash + "&ie=UTF-8&oe=UTF-8&features=Rank&q=info:" + domain;
        try {
            URLConnection con = new URL(url).openConnection();
            InputStream is = con.getInputStream();
            byte[] buff = new byte[1024];
            int read = is.read(buff);
            while (read > 0) {
                googlePrResult = new String(buff, 0, read);
                read = is.read(buff);
            }
            googlePrResult = googlePrResult.split(":")[2].trim();
            result = new Long(googlePrResult).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataCenterIdx++;
        if (dataCenterIdx == GOOGLE_PR_DATACENTER_IPS.length) {
            dataCenterIdx = 0;
        }
        return result;
    }
}
