class BackupThread extends Thread {
        public void doIntegerAttribute(MDIntegerAttribute attribute, Integer value, boolean calculated, boolean canRead, boolean canWrite) throws Exception {
            if (!currentGroupPrinted) {
                openAttributeGroup(currentGroupName, null);
            }
            xb.openElement("tr");
            renderAttributeTh(attribute);
            xb.openElement("td");
            String strValue = stringValues.get(attribute);
            String paramName = "attr_" + attribute.getName();
            String attrIdStr = "attr" + attribute.getId();
            String readonlyStr = req.getParameter("readonly_" + paramName);
            boolean isWritable = writableMap.get(attribute) && (readonlyStr == null);
            if (isWritable) {
                xb.openElement("input");
                xb.addAttribute("id", attrIdStr);
                xb.addAttribute("type", "text");
                xb.addAttribute("name", paramName);
                xb.addAttribute("value", strValue);
                xb.addAttribute("class", "text");
                String errorMsg = Util.getLocalizedString(Defs.MDLIBI18N, locale, "Invalid_value_An_integer_is_expected");
                String onBlurScript = "ctrTypeInteger(this,'" + Escape.javascriptEscape(errorMsg) + "');";
                xb.addAttribute("onblur", onBlurScript);
                xb.addAttribute("onfocus", "backgroundDelete(this);");
                xb.closeElement("input");
                renderAttributeDescription(attribute);
                handleFocus(attrIdStr);
            } else {
                xb.openElement("div");
                xb.addAttribute("id", attrIdStr);
                xb.addAttribute("class", "value");
                xb.write(strValue);
                xb.writeInputHidden(paramName, strValue);
                xb.writeInputHidden("readonly_" + paramName, "");
                xb.closeElement("div");
            }
            xb.closeElement("td");
            xb.closeElement("tr");
        }
}
