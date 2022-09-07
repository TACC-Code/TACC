class BackupThread extends Thread {
    private static byte[] readRawData(InputStream in, int estlength, Object[] decode) throws IOException {
        byte[] buf = new byte[8 * 1024];
        ByteArrayOutputStream bout = new ByteArrayOutputStream(estlength);
        for (int hunk; (hunk = in.read(buf)) != -1; ) bout.write(buf, 0, hunk);
        bout.close();
        byte[] rawdata = bout.toByteArray();
        if (decode != null) {
            double[] da = new double[decode.length];
            for (int i = 0, imax = decode.length; i < imax; i++) da[i] = ((Number) decode[i]).doubleValue();
            if (da.length == 2 && da[0] == 1.0 && da[1] == 0.0) {
                for (int i = 0, imax = rawdata.length; i < imax; i++) rawdata[i] ^= 0xff;
            }
        }
        return rawdata;
    }
}
