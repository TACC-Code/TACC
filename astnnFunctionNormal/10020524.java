class BackupThread extends Thread {
        protected String createClientAttributesKey(final Page.Request pageRequest, final String name, final boolean perPage, final List<String> qualifiers) {
            final ArrayList<String> keyComponents = new ArrayList<String>(8 + ((qualifiers != null) ? qualifiers.size() : 0));
            keyComponents.add(getPortal().getPortalID());
            keyComponents.add(getChannel().getClass().getSimpleName());
            keyComponents.add(getClass().getSimpleName());
            keyComponents.add(name);
            keyComponents.add((perPage) ? pageRequest.getPage().getKey().getOwnerID() : definition.getContentContainer().getOwnerID());
            if ((perPage) || (page != null)) keyComponents.add((perPage) ? pageRequest.getPage().getKey().getPageGroupID() : page.getKey().getPageGroupID());
            if ((perPage) || (page != null)) keyComponents.add((perPage) ? pageRequest.getPage().getKey().getPageID() : page.getKey().getPageID());
            keyComponents.add(definition.getParentDefinition().getID());
            if (qualifiers != null) keyComponents.addAll(qualifiers);
            return ConversionUtil.invokeConverter(StringUtil.COMPONENTS_TO_DASH_DELIMITED_STRING_CONVERTER, keyComponents);
        }
}
