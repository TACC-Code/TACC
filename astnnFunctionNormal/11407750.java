class BackupThread extends Thread {
    protected boolean destinationDefinitionsMatch(DestinationSettings d1, DestinationSettings d2) {
        if (d1 == null && d2 == null) return true;
        if (d1 == null || d2 == null) return fail("Destinations didn't match: {0} != {1}", new Object[] { d1, d2 });
        if (!d1.getId().equals(d2.getId())) return fail("Destination ids didn't match: {0} != {1}", new Object[] { d1.getId(), d2.getId() });
        if (!propertiesMatch(d1.getProperties(), d1.getProperties())) return false;
        List dc1 = d1.getChannelSettings();
        List dc2 = d2.getChannelSettings();
        if (dc1.size() != dc2.size()) return fail("Destination channels sections didn't match by size: {0} != {1}", new Object[] { new Integer(dc1.size()), new Integer(dc2.size()) });
        for (int i = 0; i < dc1.size(); i++) {
            ChannelSettings c1 = (ChannelSettings) dc1.get(i);
            ChannelSettings c2 = (ChannelSettings) dc2.get(i);
            if (!channelDefinitionsMatch(c1, c2)) return false;
        }
        SecurityConstraint sc1 = d1.getConstraint();
        SecurityConstraint sc2 = d2.getConstraint();
        if (!securityConstraintsMatch(sc1, sc2)) return false;
        AdapterSettings as1 = d1.getAdapterSettings();
        AdapterSettings as2 = d2.getAdapterSettings();
        if (!adapterDefinitionsMatch(as1, as2)) return false;
        return true;
    }
}
