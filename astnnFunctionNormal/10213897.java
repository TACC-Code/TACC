class BackupThread extends Thread {
    @Override
    protected void setUp() {
        engine = new FileHelperEngine<Customer>(Customer.class);
        customers = new ArrayList<Customer>();
        readCount = writeCount = 0;
        beforeReadCount = beforeWriteCount = 0;
        afterReadCount = afterWriteCount = 0;
        eventLine = "";
        notifyLine = "";
        Customer c = new Customer();
        c.custId = 1;
        c.name = "John Doe";
        c.rating = 1;
        customers.add(c);
        c = new Customer();
        c.custId = 2;
        c.name = "Jane Rowe";
        c.rating = 2;
        customers.add(c);
        c = new Customer();
        c.custId = 3;
        c.name = "Santa Claus";
        c.rating = 3;
        customers.add(c);
        c = new Customer();
        c.custId = 4;
        c.name = "Homer Simpson";
        c.rating = 4;
        customers.add(c);
    }
}
