class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public static ChannelFeed toChannel(TRss10Channel ch, RDF rdf) {
        ChannelFeed ret = new ChannelFeed();
        if (ch.getOtherAttributes() != null) {
            ret.getOtherAttributes().putAll(ch.getOtherAttributes());
        }
        UpdatePeriodEnum updatePeriod = null;
        BigInteger updateFrequency = null;
        Map<String, Integer> ordering = new HashMap<String, Integer>();
        List<ItemEntry> items = toItems(rdf.getChannelOrImageOrItem());
        ret.setAbout(ch.getAbout());
        ret.setResource(ch.getResource());
        ret.getOtherAttributes().putAll(ch.getOtherAttributes());
        for (Object o : ch.getTitleOrLinkOrDescription()) {
            if (o == null) continue;
            if (o instanceof JAXBElement) {
                JAXBElement jaxb = (JAXBElement) o;
                Object val = jaxb.getValue();
                if (same(jaxb.getName(), RSS10_TITLE)) {
                    ret.setTitle((String) jaxb.getValue());
                } else if (same(jaxb.getName(), RSS10_DESCRIPTION)) {
                    ret.setDescriptionOrSubtitle((String) jaxb.getValue());
                } else if (same(jaxb.getName(), RSS10_LINK)) {
                    ret.addLink((String) jaxb.getValue());
                } else if (same(jaxb.getName(), RSS10_UPDATEFREQUENCY)) {
                    updateFrequency = (BigInteger) jaxb.getValue();
                } else if (same(jaxb.getName(), RSS10_DC_SUBJECT)) {
                    ret.addCategorySubject(getDcTypeText(jaxb));
                } else if (same(jaxb.getName(), RSS10_DC_PUBLISHER)) {
                    ret.addManagingEditorOrAuthorOrPublisher(getDcTypeText(jaxb));
                } else if (same(jaxb.getName(), RSS10_DC_CREATOR)) {
                    ret.addWebMasterOrCreator(getDcTypeText(jaxb));
                } else if (same(jaxb.getName(), RSS10_DC_RIGHTS)) {
                    ret.setRights(getDcTypeText(jaxb));
                } else if (same(jaxb.getName(), RSS10_DC_DATE)) {
                    ret.setPubDate(getDcTypeText(jaxb));
                } else if (same(jaxb.getName(), RSS10_DC_LANGUAGE)) {
                    ret.setLang(getDcTypeText(jaxb));
                } else if (same(jaxb.getName(), RSS10_DC_CONTRIBUTOR)) {
                    ret.addContributor(getDcTypeText(jaxb));
                } else if (val instanceof UpdatePeriodEnum) {
                    updatePeriod = (UpdatePeriodEnum) val;
                } else if (val instanceof TRss10Image) {
                    Image image = new Image();
                    image.setResource(((TRss10Image) val).getResource());
                    ret.setImageOrIcon(image);
                } else if (val instanceof TRss10TextInput) {
                    TextInput in = new TextInput();
                    in.setResource(((TRss10TextInput) val).getResource());
                    ret.setTexInput(in);
                } else if (val instanceof Items) {
                    Seq seq = ((Items) val).getSeq();
                    int i = 0;
                    for (Li li : seq.getLi()) {
                        ordering.put(li.getResource(), i++);
                    }
                } else {
                    LOG.warn("Unexpected JAXBElement: " + ToStringBuilder.reflectionToString(jaxb) + " this should not happen!");
                }
            } else if (o instanceof Element) {
                Element e = (Element) o;
                ret.getOtherElements().add(e);
            } else {
                LOG.warn("Unexpected object: " + ToStringBuilder.reflectionToString(o) + " this should not happen!");
            }
        }
        ret.setTtl(calculateTtl(updatePeriod, updateFrequency));
        if (ordering.entrySet().size() != 0) {
            Collections.sort(items, new ItemComparacotr(ordering));
        }
        ret.setItems(items);
        return ret;
    }
}
