class BackupThread extends Thread {
    private void init(String roleName, String mbeanClassName, boolean read, boolean write, int min, int max, String descr) throws IllegalArgumentException, InvalidRoleInfoException {
        if (roleName == null || mbeanClassName == null) {
            String excMsg = "Invalid parameter.";
            throw new IllegalArgumentException(excMsg);
        }
        name = roleName;
        isReadable = read;
        isWritable = write;
        if (descr != null) {
            description = descr;
        }
        boolean invalidRoleInfoFlg = false;
        StringBuffer excMsgStrB = new StringBuffer();
        if (max != ROLE_CARDINALITY_INFINITY && (min == ROLE_CARDINALITY_INFINITY || min > max)) {
            excMsgStrB.append("Minimum degree ");
            excMsgStrB.append(min);
            excMsgStrB.append(" is greater than maximum degree ");
            excMsgStrB.append(max);
            invalidRoleInfoFlg = true;
        } else if (min < ROLE_CARDINALITY_INFINITY || max < ROLE_CARDINALITY_INFINITY) {
            excMsgStrB.append("Minimum or maximum degree has an illegal value, must be [0, ROLE_CARDINALITY_INFINITY].");
            invalidRoleInfoFlg = true;
        }
        if (invalidRoleInfoFlg) {
            throw new InvalidRoleInfoException(excMsgStrB.toString());
        }
        minDegree = min;
        maxDegree = max;
        referencedMBeanClassName = mbeanClassName;
        return;
    }
}
