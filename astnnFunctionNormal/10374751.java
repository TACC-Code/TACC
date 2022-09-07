class BackupThread extends Thread {
    void md4calc() {
        int tmpseq = this.seq;
        IMessageDigest md = HashFactory.getInstance("MD4");
        byte[] input = (this.seed + this.passphrase).getBytes();
        md.update(input, 0, input.length);
        byte[] digest = md.digest();
        this.hash = otpfoldregs(getregs(digest));
        while (tmpseq > 0) {
            md.update(hash, 0, hash.length);
            digest = md.digest();
            this.hash = otpfoldregs(getregs(digest));
            tmpseq--;
        }
    }
}
