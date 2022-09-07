class BackupThread extends Thread {
    private String encriptaDatoMD5(String cadenaNoEncriptada, String datoAleatorio) {
        String cadena = cadenaNoEncriptada + datoAleatorio;
        byte[] arrayBytesHash = null;
        synchronized (this.mensajeMD5) {
            this.mensajeMD5.reset();
            this.mensajeMD5.update(cadena.getBytes());
            arrayBytesHash = this.mensajeMD5.digest();
        }
        String datoHexagesinal = (this.getDatoHexagesinalMD5(arrayBytesHash) + datoAleatorio);
        return datoHexagesinal;
    }
}
