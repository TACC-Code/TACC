class BackupThread extends Thread {
    private byte[] mgf(byte[] seed, int length) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] c = new byte[4];
        int end = length / md_.getDigestLength();
        for (int counter = 0; counter <= end; counter++) {
            int tmp = counter;
            c[3] = (byte) tmp;
            tmp = tmp >> 8;
            c[2] = (byte) tmp;
            tmp = tmp >> 8;
            c[1] = (byte) tmp;
            tmp = tmp >> 8;
            c[0] = (byte) tmp;
            md_.update(seed);
            try {
                baos.write(md_.digest(c));
            } catch (IOException ioe) {
                System.out.println("shouldn't happen");
                ioe.printStackTrace();
            }
        }
        try {
            baos.flush();
        } catch (IOException ioe) {
            System.out.println("shouldn't happen");
            ioe.printStackTrace();
        }
        byte[] t = baos.toByteArray();
        byte[] out = new byte[length];
        try {
            baos.close();
        } catch (IOException ioe) {
            System.out.println("shouldn't happen");
            ioe.printStackTrace();
        }
        System.arraycopy(t, 0, out, 0, length);
        return out;
    }
}
