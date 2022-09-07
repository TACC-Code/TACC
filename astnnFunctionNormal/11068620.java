class BackupThread extends Thread {
    private boolean inicioReproduccion(int indiceMusica, boolean continuamente) {
        if (mixer == null) {
            return false;
        }
        MusicaOgg musica = musicas.elementAt(indiceMusica);
        musica.fichero = new File(musica.path);
        try {
            musica.audioInputStreamCodificada = AudioSystem.getAudioInputStream(musica.fichero);
        } catch (UnsupportedAudioFileException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        if (musica.audioInputStreamCodificada == null) {
            return false;
        }
        AudioFormat formatoOriginal = musica.audioInputStreamCodificada.getFormat();
        AudioFormat formatoDecodificado = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, formatoOriginal.getSampleRate(), 16, formatoOriginal.getChannels(), formatoOriginal.getChannels() * 2, formatoOriginal.getSampleRate(), false);
        musica.audioInputStreamDecodificada = AudioSystem.getAudioInputStream(formatoDecodificado, musica.audioInputStreamCodificada);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, formatoDecodificado);
        try {
            musica.dataLine = (SourceDataLine) mixer.getLine(info);
            if (musica.dataLine == null) {
                try {
                    musica.audioInputStreamDecodificada.close();
                    musica.audioInputStreamCodificada.close();
                } catch (IOException e) {
                }
                return false;
            }
            musica.dataLine.open(formatoDecodificado);
        } catch (LineUnavailableException e) {
            try {
                musica.audioInputStreamDecodificada.close();
                musica.audioInputStreamCodificada.close();
            } catch (IOException ioe) {
            }
            return false;
        }
        musica.dataLine.start();
        musica.controlGanancia = (FloatControl) musica.dataLine.getControl(FloatControl.Type.MASTER_GAIN);
        indiceMusicaReproduciendo = indiceMusica;
        musicaReproduciendo = musica;
        reproducirContinuamente = continuamente;
        volumenCambiado = true;
        return true;
    }
}
