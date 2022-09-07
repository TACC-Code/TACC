class BackupThread extends Thread {
    public void write(int type, Date time, OutputStream out) throws IOException, GeneralSecurityException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] hashed = new byte[5];
        byte[] num;
        long date = time.getTime() / 1000l;
        hashed[0] = (byte) type;
        hashed[1] = (byte) ((date >> 24) & 0xFF);
        hashed[2] = (byte) ((date >> 16) & 0xFF);
        hashed[3] = (byte) ((date >> 8) & 0xFF);
        hashed[4] = (byte) (date & 0xFF);
        md.update(hashed);
        sig.update(hashed);
        b.write(3);
        b.write(5);
        b.write(hashed);
        b.write(keyID);
        b.write(pkAlgo);
        b.write(Algo.SHA1);
        b.write(md.digest(), 0, 2);
        hashed = sig.sign();
        switch(pkAlgo) {
            case Algo.RSA_ES:
            case Algo.RSA_S:
                MPI.write(new BigInteger(hashed), b);
                break;
            case Algo.DSA_S:
                num = new byte[hashed[3] & 0xFF];
                System.arraycopy(hashed, 4, num, 0, num.length);
                MPI.write(new BigInteger(num), b);
                num = new byte[hashed[(hashed[3] & 0xFF) + 5] & 0xFF];
                System.arraycopy(hashed, (hashed[3] & 0xFF) + 6, num, 0, num.length);
                MPI.write(new BigInteger(num), b);
        }
        hashed = b.toByteArray();
        PacketHeader.write(false, (byte) Packet.SIGNATURE, hashed.length, out);
        out.write(hashed);
    }
}
