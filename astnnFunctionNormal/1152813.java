class BackupThread extends Thread {
    private void sendRemoteZipFile() throws Exception {
        if (validParameters()) {
            URL url = new URL(storageURL + "/receiver");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            RequestUtils requestUtils = new RequestUtils();
            requestUtils.preRequestAddParameter("senderObj", "ZipFileSender");
            requestUtils.preRequestAddParameter("wfiType", "zen");
            requestUtils.preRequestAddParameter("portalURL", this.portalURL);
            requestUtils.preRequestAddParameter("wfsID", this.wfsID);
            requestUtils.preRequestAddParameter("userID", this.userID);
            requestUtils.preRequestAddParameter("newGrafName", this.newGrafName);
            requestUtils.preRequestAddParameter("newAbstName", this.newAbstName);
            requestUtils.preRequestAddParameter("newRealName", this.newRealName);
            String zipFileName = ZipUtils.getInstance().getUniqueZipFileName();
            requestUtils.preRequestAddFile("zipFileName", zipFileName);
            requestUtils.createPostRequest();
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + requestUtils.getBoundary());
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            try {
                httpURLConnection.connect();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Cannot connect to: " + storageURL, e);
            }
            OutputStream out = httpURLConnection.getOutputStream();
            byte[] preBytes = requestUtils.getPreRequestStringBytes();
            out.write(preBytes);
            out.flush();
            FileUtils.getInstance().sendFileToStream(out, sendZipFilePath);
            byte[] postBytes = requestUtils.getPostRequestStringBytes();
            out.write(postBytes);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String retMess = in.readLine() + "\n";
            while (in.ready()) {
                retMess += in.readLine() + "\n";
            }
            in.close();
            if (HttpURLConnection.HTTP_OK != httpURLConnection.getResponseCode()) {
                throw new Exception("response not HTTP_OK !");
            }
            if (!"Workflow upload successfull".equals(retMess.trim())) {
                throw new Exception("-" + retMess + "-" + ("Workflow upload successfull".equals(retMess)));
            }
        } else {
            throw new Exception("Zip file not exist ! or not valid parameters: portalURL, wsfID, userID !");
        }
    }
}
