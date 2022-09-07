class BackupThread extends Thread {
    private String[] processRDFModel(final URL url) throws IOException {
        final Set<String> types = new HashSet<String>();
        final Model model = new ModelMem().read(url.openStream(), "");
        final Map<String, String> labels = new HashMap<String, String>();
        addLabels(model, RDFS.Class, labels);
        addLabels(model, RDF.Property, labels);
        for (ResIterator rit = model.listSubjectsWithProperty(RDF.type, RDFS.Class); rit.hasNext(); ) types.add(getLabel(labels, rit.nextResource().getURI()));
        for (ResIterator rit = model.listSubjectsWithProperty(RDF.type, RDF.Property); rit.hasNext(); ) {
            final Resource property = rit.nextResource();
            final String relation = getLabel(labels, property.getURI());
            types.add(relation);
            String domain = null;
            for (StmtIterator sit = property.listProperties(RDFS.domain); sit.hasNext(); ) domain = sit.nextStatement().getResource().getURI();
            String range = null;
            for (StmtIterator sit = property.listProperties(RDFS.range); sit.hasNext(); ) range = sit.nextStatement().getResource().getURI();
            if (!(domain == null || relation == null || range == null)) Runes.registerAlias(relation, getLabel(labels, domain) + '|' + getLabel(labels, range) + '|' + relation);
        }
        return types.toArray(new String[types.size()]);
    }
}
