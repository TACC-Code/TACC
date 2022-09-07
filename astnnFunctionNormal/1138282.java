class BackupThread extends Thread {
    private synchronized String sendAttachFile(List<String> fileIdList, boolean isInline, String contentType, String filename, byte[] data) throws IOException {
        MultipartEntity multi = new MultipartEntity();
        int i = 0;
        for (Iterator<String> iterator = fileIdList.iterator(); iterator.hasNext(); i++) {
            String fileId = (String) iterator.next();
            try {
                multi.addPart("tmpfile(" + i + ").id", new StringBody(fileId, Charset.forName("UTF-8")));
            } catch (Exception e) {
                log.error("sendAttachFile (" + i + ")", e);
            }
        }
        multi.addPart("stmpfile.data", new ByteArrayBody(data, contentType, filename));
        String url = null;
        if (isInline) {
            url = ImgupUrl;
        } else {
            url = FileupUrl;
        }
        HttpPost post = new HttpPost(url + "?pwsp=" + this.getPwspQuery());
        try {
            addDumyHeader(post);
            post.setEntity(multi);
            HttpResponse res = this.executeHttp(post);
            if (res.getStatusLine().getStatusCode() != 200) {
                log.warn("attachefile error. " + filename + "/" + res.getStatusLine().getStatusCode() + "/" + res.getStatusLine().getReasonPhrase());
                throw new IOException(filename + " error. " + res.getStatusLine().getStatusCode() + "/" + res.getStatusLine().getReasonPhrase());
            }
            if (!isJson(res)) {
                log.warn("Fileuploadの応答がJSON形式ではありません。");
                if (res != null) log.debug(toStringBody(res));
                throw new IOException("Bad attached file");
            }
            JSONObject json = JSONObject.fromObject(toStringBody(res));
            String result = json.getJSONObject("common").getString("result");
            if (!result.equals("PW1000")) {
                log.debug(json.toString(2));
                throw new IOException("Bad fileupload[" + filename + "] response " + result);
            }
            String objName = null;
            if (isInline) {
                objName = "listPcimg";
            } else {
                objName = "file";
            }
            String fileId = json.getJSONObject("data").getJSONObject(objName).getString("id");
            fileIdList.add(fileId);
            return fileId;
        } finally {
            post.abort();
        }
    }
}
