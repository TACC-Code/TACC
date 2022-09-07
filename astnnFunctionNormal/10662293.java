class BackupThread extends Thread {
    public static Any getOrderForm(Connection con, Any values, Any retValue) throws FileNotFoundException, IOException {
        String sForm = ResourceBundle.getBundle(OrderServ.class.getName()).getString("INFORM");
        String sDir = ResourceBundle.getBundle(OrderServ.class.getName()).getString("FORMDIR");
        FileInputStream fi = new FileInputStream(sDir + File.separatorChar + sForm);
        byte[] bData = new byte[(int) fi.getChannel().size()];
        fi.read(bData);
        fi.close();
        retValue.insert_Value(ObjUtil.compress(bData));
        return retValue;
    }
}
