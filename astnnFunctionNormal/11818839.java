class BackupThread extends Thread {
    protected Id createGUID() {
        StringBuffer guid = new StringBuffer(128);
        try {
            long time = System.currentTimeMillis();
            guid.append(HOST_ID);
            guid.append(":");
            guid.append(Long.toString(time));
            guid.append(":");
            guid.append(Long.toString(CURRENT_INDEX++));
            byte[] array = DigestUtil.digest(guid.toString().getBytes());
            return createAndAddId(DigestUtil.asHexString(array));
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
        return null;
    }
}
