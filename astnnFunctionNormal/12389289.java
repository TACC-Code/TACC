class BackupThread extends Thread {
        public void doDateAttribute(MDDateAttribute attribute, Date value, boolean calculated, boolean canRead, boolean canWrite) throws Exception {
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
                String errorMsg = Util.getLocalizedString(Defs.MDLIBI18N, locale, "Invalid_value_A_date_is_expected");
                String localizedPattern = attribute.getLocalizedPattern();
                String onBlurScript = "ctrTypeDate(this, '" + localizedPattern + "','" + Escape.javascriptEscape(errorMsg) + "');";
                xb.addAttribute("onblur", onBlurScript);
                xb.addAttribute("onfocus", "backgroundDelete(this);");
                xb.closeElement("input");
                xb.write(" (" + localizedPattern + ") ");
                String calImgLink = Util.getAbsoluteLink(req, "/jscalendar/img.gif");
                xb.writeImage(calImgLink, "calendar", "calendar", "cal" + attrIdStr, "calendar");
                String dateFormat = localizedPattern.toUpperCase();
                dateFormat = dateFormat.replaceAll("DD", "%d");
                dateFormat = dateFormat.replaceAll("YYYY", "%Y");
                dateFormat = dateFormat.replaceAll("MM", "%m");
                xb.writeNoHtmlEscape("<script type=\"text/javascript\"> " + "Calendar.setup({ " + "inputField     :    \"" + attrIdStr + "\"," + "ifFormat       :    \"" + dateFormat + "\"," + "button         :    \"cal" + attrIdStr + "\"" + "}); </script>");
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
