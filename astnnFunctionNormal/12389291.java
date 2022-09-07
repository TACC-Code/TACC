class BackupThread extends Thread {
        public void doWfAttribute(MDWfAttribute attribute, MDWfState value, boolean calculated, boolean canRead, boolean canWrite) throws Exception {
            if (!currentGroupPrinted) {
                openAttributeGroup(currentGroupName, null);
            }
            xb.openElement("tr");
            renderAttributeTh(attribute);
            xb.openElement("td");
            MDWfState localValue = (MDWfState) localValues.get(attribute);
            String paramName = "attr_" + attribute.getName();
            String attrIdStr = "attr" + attribute.getId();
            String readonlyStr = req.getParameter("readonly_" + paramName);
            boolean isWritable = writableMap.get(attribute) && (readonlyStr == null);
            if (isWritable) {
                xb.openElement("select");
                xb.addAttribute("id", attrIdStr);
                xb.addAttribute("name", paramName);
                if (!attribute.isRequired()) {
                    boolean selected = (value == null);
                    xb.writeOption("", selected, Util.getLocalizedString(Defs.MDLIBI18N, locale, "Undefined_state"));
                }
                for (MDWfState state : attribute.getWfStates()) {
                    boolean selected = (value == state);
                    xb.writeOption("" + state.getId(), selected, state.getName());
                }
                xb.closeElement("select");
                renderAttributeDescription(attribute);
                handleFocus(attrIdStr);
            } else {
                xb.openElement("div");
                xb.addAttribute("id", attrIdStr);
                xb.addAttribute("class", "value");
                xb.write(attribute.formatValue(localValue));
                xb.writeInputHidden(paramName, (localValue == null) ? "" : "" + localValue.getId());
                xb.writeInputHidden("readonly_" + paramName, "");
                xb.closeElement("div");
            }
            xb.closeElement("td");
            xb.closeElement("tr");
        }
}
