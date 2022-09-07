class BackupThread extends Thread {
    public boolean runTest() throws Exception {
        VDouble val1 = new VDouble(1.1234d);
        VDouble val2 = new VDouble(1.1234d);
        VDouble val3 = new VDouble(2.2345d);
        if (!val1.equalsSameType(val2)) {
            setErrorMessage("Failed 1==1");
            return false;
        }
        if (val1.equalsSameType(val3)) {
            setErrorMessage("Failed 1!=2");
            return false;
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream dbuf = new DataOutputStream(buf);
        val1.writeToStream(dbuf);
        DataInputStream bufin = new DataInputStream(new ByteArrayInputStream(buf.toByteArray()));
        Value val4 = Value.readFromStream(bufin);
        if (!val1.equalsSameType(val4)) {
            setErrorMessage("Failed 1==1 after read and write");
            return false;
        }
        if (new VDouble(1.1234d).toInteger() != 1) {
            setErrorMessage("Failed integer conversion");
            return false;
        }
        if (new VDouble(1.1234d).toLong() != 1l) {
            setErrorMessage("Failed long conversion");
            return false;
        }
        if (!new VDouble(1.1234d).toString().equals("1.1234")) {
            setErrorMessage("Failed string conversion");
            return false;
        }
        return true;
    }
}
