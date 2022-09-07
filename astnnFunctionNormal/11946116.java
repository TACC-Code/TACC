class BackupThread extends Thread {
    public void run() {
        receptor.log("Arrancando repetidor en puerto " + puerto);
        while (seguirVivo) {
            try {
                conexionActiva = false;
                servidor = new ServerSocket(puerto);
                servidor.setSoTimeout(timeout);
                conexion = servidor.accept();
                conexionActiva = true;
                log("Conexion activa en el repetidor del puerto " + puerto);
                dataIn = new DataInputStream(conexion.getInputStream());
                dataOut = new DataOutputStream(conexion.getOutputStream());
                while (true) {
                    while (dataIn.available() > 0) {
                        dataOut.writeByte(dataIn.readByte());
                        dataOut.flush();
                    }
                    Thread.sleep(frecuenciaChequeo);
                }
            } catch (Exception e) {
                e.printStackTrace(receptor.server.loggerError);
                conexionActiva = false;
            }
        }
    }
}
