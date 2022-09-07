class BackupThread extends Thread {
    @Override
    protected void update(float frequency) {
        mainSyncLFODepth = mainSyncMod.getDepth(0);
        mainSyncEnvDepth = mainSyncMod.getDepth(1);
        mainWidthLFODepth = mainPWMMod.getDepth(0);
        mainWidthEnvDepth = mainPWMMod.getDepth(1);
        subWidthLFODepth = subPWMMod.getDepth(0);
        subWidthEnvDepth = subPWMMod.getDepth(1);
        cutoffDepths = cutoffMod.getDepths();
        slowCutoffMod = amplitude * cutoffDepths[2] + getChannelPressure() / 128 * cutoffDepths[3] + getController(Controller.MODULATION) / 128 * cutoffDepths[4];
        lfo.update();
        mainOsc.update(frequency);
        subOsc.update(frequency * 0.5f);
        slowCutoffMod += filter.update();
        ampLevel = ampVars.getLevel() * ampT;
    }
}
