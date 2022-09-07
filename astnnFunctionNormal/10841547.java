class BackupThread extends Thread {
    public Item doRespondNegotiateDeadline(String itemId, Date newDeadline) throws UnsupportedEncodingException, IOException {
        log(INFO, "Respond negotiated deadline: Item id=" + itemId);
        String sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_ID");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        RespondNegotiateDeadlineRequest request = new RespondNegotiateDeadlineRequest();
        request.setItemID(itemId);
        request.setSessionId(sessionId);
        request.setNewProposedDeadline(newDeadline);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("RespondNegotiateDeadlineRequest", RespondNegotiateDeadlineRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("RespondNegotiateDeadlineResponse", RespondNegotiateDeadlineResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/respondNegotiateDeadline?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            RespondNegotiateDeadlineResponse oResponse = (RespondNegotiateDeadlineResponse) reader.fromXML(result);
            return oResponse.getItem();
        }
        return null;
    }
}
