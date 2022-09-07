class BackupThread extends Thread {
    private void assertAuthorized(Connection conn, String id, int host, String partner, Collection<Integer> properties, int right) throws AuthorizationFault, ExistenceFault, GeneralFault, SQLException {
        if (right == PropertyRight.NONE || partner == null) {
            assertHasProperties(conn, id, host, properties);
        } else {
            Collection<Integer> props;
            props = getAuthorizedProps(conn, id, host, partner, properties, right);
            if (properties.size() != props.size()) {
                properties.removeAll(props);
                Set<String> ps = getProperties(conn, id, host);
                LinkedList<String> unauthProps = new LinkedList<String>();
                LinkedList<String> unexistProps = new LinkedList<String>();
                for (Integer p : properties) {
                    String extRep = representer.getExternalRep(p);
                    if (ps.contains(extRep)) {
                        unauthProps.add(extRep);
                    } else {
                        unexistProps.add(extRep);
                    }
                }
                if (unexistProps.size() == 0) {
                    throw new AuthorizationFault("You are not authorized to read/write the " + StringUtils.join(unauthProps.iterator(), ", ") + " properties of item " + getIdUri(id, host));
                } else {
                    throw new ExistenceFault("Properties " + StringUtils.join(unexistProps.iterator(), ", ") + " of item " + getIdUri(id, host) + " does not exist.");
                }
            }
        }
    }
}
