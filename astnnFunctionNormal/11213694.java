class BackupThread extends Thread {
    private JgrliResult call(JgrliMethod method, NameValuePair... parameterArray) throws JgrliException {
        JgrliResult ret = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("apikey", this.apiKey));
            if (this.providerKey != null) {
                parameters.add(new BasicNameValuePair("providerkey", this.providerKey));
            }
            parameters.addAll(Arrays.asList(parameterArray));
            URI uri = URIUtils.createURI(SERVER_SCHEME, SERVER_HOST, SERVER_PORT, SERVER_BASE_FOLDER + "/" + method.getId(), URLEncodedUtils.format(parameters, "UTF-8"), null);
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                try {
                    SAXBuilder saxBuilder = new SAXBuilder();
                    Document document = saxBuilder.build(inputStream);
                    Element rootElement = document.getRootElement();
                    String rootElementName = rootElement.getName();
                    if (!"prowl".equals(rootElementName)) {
                        throw new JgrliException("Unknown root element name delivered by prowl (" + rootElementName + "!");
                    }
                    List<?> children = rootElement.getChildren();
                    if (children.size() != 1) {
                        throw new JgrliException("Prowl result contains more than one element!");
                    }
                    Element prowlElement = (Element) children.get(0);
                    String elementName = prowlElement.getName();
                    Attribute codeAttribute = prowlElement.getAttribute("code");
                    String stringCode = codeAttribute.getValue();
                    int code = Integer.parseInt(stringCode);
                    JgrliErrorCode prowlErrorCode = JgrliErrorCode.parse(code);
                    if ("error".equals(elementName)) {
                        String message = prowlElement.getText();
                        ret = new JgrliErrorResult(prowlErrorCode, message);
                    } else if ("success".equals(elementName)) {
                        String stringRemaining = prowlElement.getAttributeValue("remaining");
                        String stringResetDate = prowlElement.getAttributeValue("resetdate");
                        long resetDateTimestamp = Long.parseLong(stringResetDate);
                        int remaining = Integer.parseInt(stringRemaining);
                        Date resetDate = new Date(resetDateTimestamp * 1000);
                        ret = new JgrliSuccessResult(prowlErrorCode, remaining, resetDate);
                    } else {
                        throw new JgrliException("Unknown prowl return content (" + elementName + ")!");
                    }
                } catch (JDOMException e) {
                    throw new JgrliException("Unable to parse result!", e);
                }
            }
        } catch (URISyntaxException e) {
            throw new JgrliException("Unable to encode URL for prowl call!", e);
        } catch (ClientProtocolException e) {
            throw new JgrliException("Unable to process for HTTP prowl call!", e);
        } catch (IOException e) {
            throw new JgrliException("Unable to process prowl call!", e);
        }
        return ret;
    }
}
