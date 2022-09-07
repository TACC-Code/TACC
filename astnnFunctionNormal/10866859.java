class BackupThread extends Thread {
    public Item doDelegate(String parentId, String targets, String subject, String task, Date startDate, Date deadline, String links, String tags, FileRepository fileRepository) throws java.text.ParseException, UnsupportedEncodingException, IOException {
        log(INFO, "Delegate item: Parent id=" + parentId);
        String sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_ID");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        DelegateItemRequest request = new DelegateItemRequest();
        request.setDeadline(deadline);
        request.setPriority(Priority.NOT_SET);
        String[] recip = targets.split(",");
        request.setRecipients(Arrays.asList(recip));
        request.setSessionId(sessionId);
        request.setStartDate(startDate);
        request.setTask(task);
        request.setParentId(parentId);
        request.setFileRepository(fileRepository);
        request.setSubject(subject);
        if (links == null || ("").equals(links.trim())) {
        } else {
            String[] alinks = links.split(",");
            List<String> linkList = new ArrayList<String>();
            linkList.addAll(Arrays.asList(alinks));
            request.setLinks(linkList);
        }
        if (tags == null || ("").equals(tags.trim())) {
        } else {
            String[] atags = tags.split(",");
            List<String> tagList = new ArrayList<String>();
            tagList.addAll(Arrays.asList(atags));
            request.setTags(tagList);
        }
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("DelegateItemRequest", DelegateItemRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("DelegateItemResponse", DelegateItemResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/delegateItem?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        DelegateItemResponse oResponse = null;
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            oResponse = (DelegateItemResponse) reader.fromXML(result);
        }
        return oResponse.getParentItem();
    }
}
