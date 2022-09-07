class BackupThread extends Thread {
    public void write(int type, byte[] hashed, byte[] unhashed, OutputStream out) throws IOException, GeneralSecurityException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] num = new byte[2];
        byte[] sb = { (byte) 4, (byte) type, (byte) pkAlgo, (byte) Algo.SHA1, (byte) ((hashed.length >> 8) & 0xFF), (byte) (hashed.length & 0xFF) };
        int len = 6 + hashed.length;
        md.update(sb);
        sig.update(sb);
        b.write(sb);
        md.update(hashed);
        sig.update(hashed);
        b.write(hashed);
        sb = new byte[6];
        sb[0] = (byte) 4;
        sb[1] = (byte) 0xFF;
        sb[2] = (byte) ((len >> 24) & 0xFF);
        sb[3] = (byte) ((len >> 16) & 0xFF);
        sb[4] = (byte) ((len >> 8) & 0xFF);
        sb[5] = (byte) (len & 0xFF);
        md.update(sb);
        sig.update(sb);
        num[0] = (byte) ((unhashed.length >> 8) & 0xFF);
        num[1] = (byte) (unhashed.length & 0xFF);
        b.write(num);
        b.write(unhashed);
        b.write(md.digest(), 0, 2);
        sb = sig.sign();
        switch(pkAlgo) {
            case Algo.RSA_ES:
            case Algo.RSA_S:
                MPI.write(new BigInteger(sb), b);
                break;
            case Algo.DSA_S:
                num = new byte[sb[3] & 0xFF];
                System.arraycopy(sb, 4, num, 0, num.length);
                MPI.write(new BigInteger(num), b);
                num = new byte[sb[(sb[3] & 0xFF) + 5] & 0xFF];
                System.arraycopy(sb, (sb[3] & 0xFF) + 6, num, 0, num.length);
                MPI.write(new BigInteger(num), b);
        }
        sb = b.toByteArray();
        PacketHeader.write(false, (byte) Packet.SIGNATURE, sb.length, out);
        out.write(sb);
    }
}
