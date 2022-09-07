class BackupThread extends Thread {
    @GET
    @Path(RESOURCE_MODE + "{ChannelLocalPath:.*}")
    public final Response doResourceGetRequest(@Context final Request rsRequest, @Context final UriInfo uriInfo, @Context final SecurityContext securityContext, @Context final HttpHeaders httpHeaders, @RequestAttributes final Map<String, Object> requestAttributes, @SessionAttributes final Map<String, Object> sessionAttributes) throws WWWeeePortal.Exception, WebApplicationException {
        final Page page = (Page) uriInfo.getMatchedResources().get(2);
        return doResourceRequest(page.new Request(rsRequest, uriInfo, securityContext, httpHeaders, requestAttributes, sessionAttributes, null, page, ContentManager.ChannelSpecification.getKey(page.getDefinition().getChannelSpecification(definition.getID())), null));
    }
}
