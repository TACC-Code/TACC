class BackupThread extends Thread {
    public void login() throws MalformedURLException, IOException, UnexpectedResponseCodeException, UnauthorizedResponseException, ForbiddenResponseException, BadRequestResponseException {
        List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();
        parametersBody.add(new BasicNameValuePair("loginName", username));
        parametersBody.add(new BasicNameValuePair("password", password));
        parametersBody.add(new BasicNameValuePair("returnURL", "https://secure.eu.playstation.com/sign-in/confirmation/"));
        HttpPost postReq = new HttpPost("https://store.playstation.com/external/login.action");
        postReq.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));
        HttpResponse response = httpClient.execute(postReq, context);
        response.getEntity().consumeContent();
        switch(response.getStatusLine().getStatusCode()) {
            case 302:
                HttpGet getReq = new HttpGet(response.getHeaders("Location")[0].getValue());
                response = httpClient.execute(getReq, context);
                response.getEntity().consumeContent();
                isLoggedIn = true;
                break;
            case 200:
                throw new UnauthorizedResponseException();
            case 401:
            case 403:
                throw new ForbiddenResponseException();
            case 400:
            case 402:
            case 404:
            case 405:
            case 406:
            case 410:
                throw new BadRequestResponseException();
            default:
                throw new UnexpectedResponseCodeException(response.getStatusLine().getStatusCode());
        }
    }
}
