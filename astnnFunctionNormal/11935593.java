class BackupThread extends Thread {
    public void go() throws FBConnectionException, FBErrorException, IOException {
        clearError();
        URL url = new URL(getHost() + getPath());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("X-FB-User", getUser());
        conn.setRequestProperty("X-FB-Auth", makeResponse());
        conn.setRequestProperty("X-FB-Mode", "GetSecGroups");
        conn.connect();
        Element fbresponse;
        try {
            fbresponse = readXML(conn);
        } catch (FBConnectionException fbce) {
            throw fbce;
        } catch (FBErrorException fbee) {
            throw fbee;
        } catch (Exception e) {
            FBConnectionException fbce = new FBConnectionException("XML parsing failed");
            fbce.attachSubException(e);
            throw fbce;
        }
        NodeList nl = fbresponse.getElementsByTagName("GetSecGroupsResponse");
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element && hasError((Element) nl.item(i))) {
                error = true;
                FBErrorException e = new FBErrorException();
                e.setErrorCode(errorcode);
                e.setErrorText(errortext);
                throw e;
            }
        }
        nl = fbresponse.getElementsByTagName("SecGroup");
        for (int i = 0; i < nl.getLength(); i++) {
            NamedNodeMap nnm = nl.item(i).getAttributes();
            int tempid = 0;
            try {
                tempid = Integer.parseInt(nnm.getNamedItem("id").getNodeValue());
            } catch (Exception e) {
                continue;
            }
            String tempname = DOMUtil.getSimpleElementText((Element) nl.item(i), "Name");
            security.addGroup(tempid, tempname);
        }
        return;
    }
}
