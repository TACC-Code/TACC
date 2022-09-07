class BackupThread extends Thread {
        public void doTextAttribute(MDTextAttribute attribute, String value, boolean calculated, boolean canRead, boolean canWrite) throws Exception {
            if (!currentGroupPrinted) {
                openAttributeGroup(currentGroupName, null);
            }
            xb.openElement("tr");
            renderAttributeTh(attribute);
            xb.openElement("td");
            String localValue = (String) localValues.get(attribute);
            String paramName = "attr_" + attribute.getName();
            String attrIdStr = "attr" + attribute.getId();
            String readonlyStr = req.getParameter("readonly_" + paramName);
            boolean isWritable = writableMap.get(attribute) && (readonlyStr == null);
            if (isWritable) {
                if (attribute.isMultiline()) {
                    String funzione = "verifyLine(this);";
                    xb.openElement("textarea");
                    xb.addAttribute("id", attrIdStr);
                    xb.addAttribute("name", paramName);
                    xb.addAttribute("cols", "" + Defs.TEXTAREA_NUM_COLS);
                    xb.addAttribute("rows", "" + numRowTextArea(localValue, Defs.TEXTAREA_NUM_COLS));
                    xb.addAttribute("onkeyup", funzione);
                    if (localValue != null) xb.write(localValue);
                    xb.closeElement("textarea");
                } else {
                    xb.openElement("input");
                    xb.addAttribute("id", attrIdStr);
                    xb.addAttribute("type", attribute.isPassword() ? "password" : "text");
                    xb.addAttribute("name", paramName);
                    xb.addAttribute("value", (localValue == null) ? "" : localValue);
                    xb.addAttribute("class", "text");
                    xb.addAttribute("size", "" + (attribute.getLength() > Defs.TEXTAREA_NUM_COLS ? Defs.TEXTAREA_NUM_COLS : attribute.getLength()));
                    xb.addAttribute("maxlength", "" + attribute.getLength());
                    xb.closeElement("input");
                }
                renderAttributeDescription(attribute);
                handleFocus(attrIdStr);
            } else {
                String formattedValue = attribute.formatValue(localValue);
                xb.openElement("div");
                xb.addAttribute("id", attrIdStr);
                xb.addAttribute("class", "value");
                xb.write(formattedValue);
                xb.writeInputHidden(paramName, formattedValue);
                xb.writeInputHidden("readonly_" + paramName, "");
                xb.closeElement("div");
            }
            xb.closeElement("td");
            xb.closeElement("tr");
        }
}
