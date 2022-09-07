class BackupThread extends Thread {
    public HttpResponse execute(HttpRequest request) throws GadgetException {
        normalizeProtocol(request);
        HttpResponse invalidatedResponse = null;
        if (!request.getIgnoreCache()) {
            HttpResponse cachedResponse = httpCache.getResponse(request);
            if (cachedResponse != null) {
                if (!cachedResponse.isStale()) {
                    if (invalidationService.isValid(request, cachedResponse)) {
                        return cachedResponse;
                    } else {
                        invalidatedResponse = cachedResponse;
                    }
                }
            }
        }
        HttpResponse fetchedResponse = null;
        switch(request.getAuthType()) {
            case NONE:
                fetchedResponse = httpFetcher.fetch(request);
                break;
            case SIGNED:
            case OAUTH:
                fetchedResponse = oauthRequestProvider.get().fetch(request);
                break;
            default:
                return HttpResponse.error();
        }
        if (fetchedResponse.isError() && invalidatedResponse != null) {
            return invalidatedResponse;
        }
        if (!fetchedResponse.isError() && !request.getIgnoreCache() && request.getCacheTtl() != 0) {
            fetchedResponse = imageRewriter.rewrite(request, fetchedResponse);
        }
        if (!request.getIgnoreCache()) {
            if (fetchedResponse.getCacheTtl() > 0) {
                fetchedResponse = invalidationService.markResponse(request, fetchedResponse);
            }
            httpCache.addResponse(request, fetchedResponse);
        }
        return fetchedResponse;
    }
}
