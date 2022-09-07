class BackupThread extends Thread {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (PublisherPublicKeyDigest.class == obj.getClass()) {
            if (PublisherType.KEY == this.type()) return (Arrays.equals(_publisherID, ((PublisherPublicKeyDigest) obj).digest()));
            throw new UnsupportedOperationException("Have to finish up equals!");
        }
        if (getClass() != obj.getClass()) return false;
        final PublisherID other = (PublisherID) obj;
        if (!Arrays.equals(_publisherID, other._publisherID)) return false;
        if (_publisherType == null) {
            if (other.type() != null) return false;
        } else if (!_publisherType.equals(other.type())) return false;
        return true;
    }
}
