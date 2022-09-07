class BackupThread extends Thread {
    @POST
    @Path(VIEW_MODE + "{ChannelLocalPath:.*}")
    @Produces("application/xhtml+xml,*/*")
    public final Response doViewPostRequest(@Context final Request rsRequest, @Context final UriInfo uriInfo, @Context final SecurityContext securityContext, @Context final HttpHeaders httpHeaders, @RequestAttributes final Map<String, Object> requestAttributes, @SessionAttributes final Map<String, Object> sessionAttributes, final DataSource entity) throws WWWeeePortal.Exception, WebApplicationException {
        final Page page = (Page) uriInfo.getMatchedResources().get(2);
        return page.doViewRequest(rsRequest, uriInfo, securityContext, httpHeaders, requestAttributes, sessionAttributes, entity, ContentManager.ChannelSpecification.getKey(page.getDefinition().getChannelSpecification(definition.getID())));
    }
}
