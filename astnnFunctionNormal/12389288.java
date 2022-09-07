class BackupThread extends Thread {
        public void doBooleanAttribute(MDBooleanAttribute attribute, Boolean value, boolean calculated, boolean canRead, boolean canWrite) throws Exception {
            if (!currentGroupPrinted) {
                openAttributeGroup(currentGroupName, null);
            }
            xb.openElement("tr");
            renderAttributeTh(attribute);
            xb.openElement("td");
            Boolean localValue = (Boolean) localValues.get(attribute);
            String paramName = "attr_" + attribute.getName();
            String attrIdStr = "attr" + attribute.getId();
            String readonlyStr = req.getParameter("readonly_" + paramName);
            boolean isWritable = writableMap.get(attribute) && (readonlyStr == null);
            if (isWritable) {
                xb.openElement("input");
                xb.addAttribute("id", attrIdStr);
                xb.addAttribute("type", "checkbox");
                xb.addAttribute("class", "checkbox");
                xb.addAttribute("name", paramName);
                if (Boolean.TRUE.equals(localValue)) xb.addAttribute("checked", "checked");
                xb.closeElement("input");
                xb.openElement("input");
                xb.addAttribute("type", "hidden");
                xb.addAttribute("name", paramName + "_ctrlcheckbox");
                xb.addAttribute("value", "true");
                xb.closeElement("input");
                renderAttributeDescription(attribute);
                handleFocus(attrIdStr);
            } else {
                xb.openElement("div");
                xb.addAttribute("id", attrIdStr);
                xb.addAttribute("class", "value");
                xb.write(attribute.formatValue(localValue));
                if (Boolean.TRUE.equals(localValue)) {
                    xb.writeInputHidden(paramName, "");
                }
                xb.writeInputHidden(paramName + "_ctrlcheckbox", "");
                xb.writeInputHidden("readonly_" + paramName, "");
                xb.closeElement("div");
            }
            xb.closeElement("td");
            xb.closeElement("tr");
        }
}
