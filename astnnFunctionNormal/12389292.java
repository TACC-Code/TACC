class BackupThread extends Thread {
        public void doRelAttribute(MDRelAttribute attribute, Integer value, boolean calculated, boolean canRead, boolean canWrite) throws Exception {
            if (!currentGroupPrinted) {
                openAttributeGroup(currentGroupName, null);
            }
            xb.openElement("tr");
            renderAttributeTh(attribute);
            xb.openElement("td");
            Integer localValue = (Integer) localValues.get(attribute);
            String paramName = "attr_" + attribute.getName();
            String attrIdStr = "attr" + attribute.getId();
            String readonlyStr = req.getParameter("readonly_" + paramName);
            boolean isWritable = writableMap.get(attribute) && (readonlyStr == null);
            if (isWritable) {
                Breadcrumbs.doContext(req, xb, cls, attribute, "attr", obj, true, false);
                xb.writeInputHidden("chk_" + paramName, "");
                renderAttributeDescription(attribute);
                handleFocus(attrIdStr);
            } else {
                xb.openElement("div");
                xb.addAttribute("id", attrIdStr);
                xb.addAttribute("class", "value");
                if (localValue == null) {
                    xb.writeInputHidden(paramName, "");
                } else {
                    MDObject localObject = attribute.getOppositeEndCls().getMDObject(localValue);
                    xb.write(Util.firstLetterToUpper(localObject.getName()));
                    xb.writeInputHidden(paramName, localValue.toString());
                }
                xb.writeInputHidden("readonly_" + paramName, "");
                xb.writeInputHidden("chk_" + paramName, "");
                xb.closeElement("div");
            }
            xb.closeElement("td");
            xb.closeElement("tr");
        }
}
