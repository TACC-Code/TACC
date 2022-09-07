class BackupThread extends Thread {
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        String[] jsfiles = { "plugins/sexycombo/jquery.sexy-combo-2.0.6.js" };
        String[] cssfiles = { "plugins/sexycombo/sexy-combo.css", "plugins/sexycombo/sexy.css" };
        JSFUtility.renderScriptOnce(writer, context, this, jsfiles, cssfiles, REQUEST_MAP_SEXCOMBO);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        Map attr = this.getAttributes();
        writer.writeText("$(document).ready(function(){\n", null);
        if (attr.get(JSCREATE) != null && (Boolean) attr.get(JSCREATE) == true) {
            writer.writeText("$.sexyCombo.create({", null);
            String clientId = getClientId(context);
            attr.put("clientId", clientId);
            boolean comma = false;
            comma = JSFUtility.writeJSObjectOption(writer, "id", attr, "clientId", String.class, comma);
            comma = JSFUtility.writeJSObjectOption(writer, NAME, attr, "clientId", String.class, comma);
            comma = JSFUtility.writeJSObjectOption(writer, MULTIPLE, attr, MULTIPLE, String.class, comma);
            if (attr.get(CONTAINER) == null || attr.get(CONTAINER).equals("")) {
                if (comma) {
                    writer.write(",");
                }
                writer.write("container:\"#" + getId() + "__container\"");
                comma = true;
            } else {
                comma = JSFUtility.writeJSObjectOption(writer, CONTAINER, attr, CONTAINER, String.class, comma);
            }
            comma = JSFUtility.writeJSObjectOption(writer, URL, attr, URL, String.class, comma);
            comma = JSFUtility.writeJSObjectOption(writer, AJAXDATA, attr, AJAXDATA, String.class, comma);
            if (comma) {
                writer.write(",");
            }
            JSFUtility.writeJSObjectOptions(writer, attr, DropDown.class);
            writer.writeText("});\n", null);
        } else {
            String clientId = getClientId(context);
            clientId = clientId.replace(":", "\\\\:");
            writer.writeText("$(\"#" + clientId + "\").sexyCombo({", null);
            JSFUtility.writeJSObjectOptions(writer, attr, DropDown.class);
            writer.writeText("});\n", null);
        }
        writer.writeText("});", null);
        writer.endElement("script");
        if (!Boolean.TRUE.equals(attr.get(JSCREATE))) {
            writer.startElement("select", this);
            writer.writeAttribute("id", getClientId(context), "DropDown");
            writer.writeAttribute("name", getClientId(context), "DropDown");
            if (getValue() != null) {
                writer.writeAttribute("value", getValue(), "value");
            }
            if (attr.get(STYLE) != null) {
                writer.writeAttribute("style", (String) attr.get(STYLE), "DropDown");
            }
            if (attr.get(STYLECLASS) != null) {
                writer.writeAttribute("class", (String) attr.get(STYLECLASS), "DropDown");
            }
        } else if (attr.get(CONTAINER) == null || attr.get(CONTAINER).equals("")) {
            writer.startElement("div", this);
            writer.writeAttribute("id", getId() + "__container", "id");
            writer.endElement("div");
        }
        writer.flush();
    }
}
