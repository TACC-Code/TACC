class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        ContactManager conM = vcard.readFile("./data/Test.vcf");
        vcard.writeFile(conM, "./data/Test_write.vcf");
        conM = vcard.readFile("./data/Test_write.vcf");
        vcard.writeFile(conM, "./data/Test_write1.vcf");
    }
}
