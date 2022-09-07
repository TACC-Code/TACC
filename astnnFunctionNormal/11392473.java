class BackupThread extends Thread {
    private Speech generateSpeech(Text text, Socket controlChannel, Socket dataChannel, String voice, String language) throws IOException, UnsupportedEncodingException {
        final String CONTROL = "control";
        final String DATA = "data";
        MixedAsciiInputStream controlIn = new MixedAsciiInputStream(new BufferedInputStream(controlChannel.getInputStream()));
        Writer controlOut = new OutputStreamWriter(controlChannel.getOutputStream(), ENCODING_ISO);
        MixedAsciiInputStream dataIn = new MixedAsciiInputStream(new BufferedInputStream(dataChannel.getInputStream()));
        Writer dataOut = new OutputStreamWriter(dataChannel.getOutputStream(), ENCODING_ISO);
        String controlHandle = getChannelHandle(controlIn, CONTROL);
        log.info("control channel handle=" + controlHandle);
        sendLineToChannel(controlIn, controlOut, "setl charset " + ENCODING, CONTROL);
        sendLineToChannel(controlIn, controlOut, "setl voice " + voice, CONTROL);
        sendLineToChannel(controlIn, controlOut, "setl language " + language, CONTROL);
        sendLineToChannel(controlIn, controlOut, "setl waveheader true", CONTROL);
        String dataHandle = getChannelHandle(dataIn, DATA);
        log.info("data channel handle=" + dataHandle);
        sendLineToChannel(dataIn, dataOut, "data " + controlHandle, DATA);
        sendLineToChannel(controlIn, controlOut, String.format("strm $%1$s:raw:rules:diphs:synth:$%1$s", dataHandle), CONTROL);
        sendLineToChannel(controlIn, controlOut, "appl " + text.getText().length(), CONTROL);
        dataOut.write(text.getText());
        dataOut.flush();
        getSize(controlIn, CONTROL);
        int dataSize = getSize(controlIn, CONTROL);
        log.info("data size: " + dataSize);
        getSize(controlIn, CONTROL);
        log.info(String.format("received(%s): %s", CONTROL, controlIn.nextLine()));
        byte[] bytes = Utils.readAllBytes(dataIn, 10000, dataSize);
        sendLineToChannel(controlIn, controlOut, "delh " + dataHandle, CONTROL);
        sendLineToChannel(controlIn, controlOut, "done", CONTROL);
        return new Speech(text.getLanguage(), "wav/signed", bytes);
    }
}
