class BackupThread extends Thread {
    @Override
    public int compareTo(MidiEvent other) {
        if (mTick != other.getTick()) {
            return mTick < other.getTick() ? -1 : 1;
        }
        if (mDelta.getValue() != other.mDelta.getValue()) {
            return mDelta.getValue() < other.mDelta.getValue() ? 1 : -1;
        }
        if (!(other instanceof ChannelEvent)) {
            return 1;
        }
        ChannelEvent o = (ChannelEvent) other;
        if (mType != o.getType()) {
            if (mOrderMap == null) {
                buildOrderMap();
            }
            int order1 = mOrderMap.get(mType);
            int order2 = mOrderMap.get(o.getType());
            return order1 < order2 ? -1 : 1;
        }
        if (mValue1 != o.mValue1) {
            return mValue1 < o.mValue1 ? -1 : 1;
        }
        if (mValue2 != o.mValue2) {
            return mValue2 < o.mValue2 ? -1 : 1;
        }
        if (mChannel != o.getChannel()) {
            return mChannel < o.getChannel() ? -1 : 1;
        }
        return 0;
    }
}
