class BackupThread extends Thread {
    protected String _computeDigest(final Collection<E> elements) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        if (elements == null || elements.size() == 0) {
            _updateDigest(digest, "");
        } else {
            ArrayList<E> list = new ArrayList<E>(elements);
            Collections.sort(list, new OvalElementComparator());
            for (Element element : list) {
                _updateDigest(digest, element);
            }
        }
        return _byteArrayToHexString(digest.digest());
    }
}
