class BackupThread extends Thread {
    @GET
    @Path(VIEW_MODE + "{ChannelLocalPath:.*}")
    @Produces("application/xhtml+xml,*/*")
    public final Response doViewGetRequest(@Context final Request rsRequest, @Context final UriInfo uriInfo, @Context final SecurityContext securityContext, @Context final HttpHeaders httpHeaders, @RequestAttributes final Map<String, Object> requestAttributes, @SessionAttributes final Map<String, Object> sessionAttributes) throws WWWeeePortal.Exception, WebApplicationException {
        final Page page = (Page) uriInfo.getMatchedResources().get(2);
        return page.doViewRequest(rsRequest, uriInfo, securityContext, httpHeaders, requestAttributes, sessionAttributes, null, ContentManager.ChannelSpecification.getKey(page.getDefinition().getChannelSpecification(definition.getID())));
    }
}
