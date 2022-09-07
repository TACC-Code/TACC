class BackupThread extends Thread {
    public static void write(org.omg.CORBA.portable.OutputStream ostream, com.sun.corba.se.PortableActivationIDL.ORBAlreadyRegistered value) {
        ostream.write_string(id());
        ostream.write_string(value.orbId);
    }
}
