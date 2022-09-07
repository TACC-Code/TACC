class BackupThread extends Thread {
    private String encriptaDatoSHA1(String cadenaNoEncriptada, String datoAleatorio) {
        String cadena = cadenaNoEncriptada + datoAleatorio;
        byte[] arrayBytesHash = null;
        synchronized (this.mensajeSHA1) {
            this.mensajeSHA1.reset();
            this.mensajeSHA1.update(cadena.getBytes());
            arrayBytesHash = this.mensajeSHA1.digest();
        }
        String datoHexagesinal = (this.getDatoHexagesinalSHA1(arrayBytesHash) + datoAleatorio);
        return datoHexagesinal;
    }
}
