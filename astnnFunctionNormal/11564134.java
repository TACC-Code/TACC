class BackupThread extends Thread {
    @Override
    protected void encodeAll(FacesContext context, RenderingContext rc, UIComponent comp, FacesBean bean) throws IOException {
        UIComponent busyFacet = comp.getFacet(CoreStatusIndicator.BUSY_FACET);
        UIComponent readyFacet = comp.getFacet(CoreStatusIndicator.READY_FACET);
        boolean iconMode = false;
        Icon busyIcon = null;
        Icon readyIcon = null;
        if (busyFacet == null && readyFacet == null) {
            busyIcon = rc.getIcon(SkinSelectors.AF_STATUS_INDICATOR_BUSY_ICON);
            readyIcon = rc.getIcon(SkinSelectors.AF_STATUS_INDICATOR_READY_ICON);
            if (busyIcon == null || readyIcon == null) {
                _LOG.warning("STATUS_INDICATOR_MISSING_ICONS");
                return;
            }
            iconMode = true;
        }
        ResponseWriter rw = context.getResponseWriter();
        String clientId = getClientId(context, comp);
        rw.startElement(XhtmlConstants.SPAN_ELEMENT, comp);
        renderId(context, comp);
        renderAllAttributes(context, rc, bean);
        rw.startElement(XhtmlConstants.SPAN_ELEMENT, null);
        rw.writeAttribute(XhtmlConstants.ID_ATTRIBUTE, clientId + "::ready", null);
        if (iconMode) {
            _renderIcon(context, rc, readyIcon, "af_statusIndicator.READY");
        } else {
            _renderFacet(context, rc, readyFacet, SkinSelectors.AF_STATUS_INDICATOR_READY_STYLE);
        }
        rw.endElement(XhtmlConstants.SPAN_ELEMENT);
        rw.startElement(XhtmlConstants.SPAN_ELEMENT, null);
        rw.writeAttribute(XhtmlConstants.ID_ATTRIBUTE, clientId + "::busy", null);
        rw.writeAttribute(XhtmlConstants.STYLE_ATTRIBUTE, "display:none", null);
        if (iconMode) {
            _renderIcon(context, rc, busyIcon, "af_statusIndicator.BUSY");
        } else {
            _renderFacet(context, rc, busyFacet, SkinSelectors.AF_STATUS_INDICATOR_BUSY_STYLE);
        }
        rw.endElement(XhtmlConstants.SPAN_ELEMENT);
        rw.startElement(XhtmlConstants.SCRIPT_ELEMENT, null);
        renderScriptTypeAttribute(context, rc);
        rw.writeText("TrStatusIndicator._register(\"" + clientId + "\");", null);
        rw.endElement(XhtmlConstants.SCRIPT_ELEMENT);
        rw.endElement(XhtmlConstants.SPAN_ELEMENT);
    }
}
