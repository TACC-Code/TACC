class BackupThread extends Thread {
    public static void write(org.omg.CORBA.portable.OutputStream ostream, com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBound value) {
        ostream.write_string(id());
    }
}
