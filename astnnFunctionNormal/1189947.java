class BackupThread extends Thread {
    protected void initAudioInputStream() throws ReproductorExcepcion {
        try {
            reset();
            notifyEvent(ReproductorEvento.ABRIENDO, getEncodedStreamPosition(), -1, mi_fuente_de_Datos);
            if (mi_fuente_de_Datos instanceof URL) {
                initAudioInputStream((URL) mi_fuente_de_Datos);
            } else if (mi_fuente_de_Datos instanceof File) {
                initAudioInputStream((File) mi_fuente_de_Datos);
            } else if (mi_fuente_de_Datos instanceof InputStream) {
                initAudioInputStream((InputStream) mi_fuente_de_Datos);
            }
            createLine();
            Map properties = null;
            if (m_audioFileFormat instanceof TAudioFileFormat) {
                properties = ((TAudioFileFormat) m_audioFileFormat).properties();
                properties = deepCopy(properties);
            } else properties = new HashMap();
            if (m_audioFileFormat.getByteLength() > 0) properties.put("audio.length.bytes", new Integer(m_audioFileFormat.getByteLength()));
            if (m_audioFileFormat.getFrameLength() > 0) properties.put("audio.length.frames", new Integer(m_audioFileFormat.getFrameLength()));
            if (m_audioFileFormat.getType() != null) properties.put("audio.type", (m_audioFileFormat.getType().toString()));
            AudioFormat audioFormat = m_audioFileFormat.getFormat();
            if (audioFormat.getFrameRate() > 0) properties.put("audio.framerate.fps", new Float(audioFormat.getFrameRate()));
            if (audioFormat.getFrameSize() > 0) properties.put("audio.framesize.bytes", new Integer(audioFormat.getFrameSize()));
            if (audioFormat.getSampleRate() > 0) properties.put("audio.samplerate.hz", new Float(audioFormat.getSampleRate()));
            if (audioFormat.getSampleSizeInBits() > 0) properties.put("audio.samplesize.bits", new Integer(audioFormat.getSampleSizeInBits()));
            if (audioFormat.getChannels() > 0) properties.put("audio.channels", new Integer(audioFormat.getChannels()));
            if (audioFormat instanceof TAudioFormat) {
                Map addproperties = ((TAudioFormat) audioFormat).properties();
                properties.putAll(addproperties);
            }
            properties.put("basicplayer.sourcedataline", m_line);
            Iterator it = m_listeners.iterator();
            while (it.hasNext()) {
                ReproductorLanzador bpl = (ReproductorLanzador) it.next();
                bpl.abierto(properties);
            }
            m_status = ABIERTO;
            notifyEvent(ReproductorEvento.ABIERTO, getEncodedStreamPosition(), -1, null);
        } catch (LineUnavailableException e) {
            throw new ReproductorExcepcion(e);
        } catch (UnsupportedAudioFileException e) {
            throw new ReproductorExcepcion(e);
        } catch (IOException e) {
            throw new ReproductorExcepcion(e);
        }
    }
}
