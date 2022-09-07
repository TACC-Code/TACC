class BackupThread extends Thread {
            public void fire() {
                value = Arithmetic.setBit(value, CAL_START, false);
                decode(value);
                value = Arithmetic.setBit(value, CAL_COMPLETE, true);
                decode(value);
                LOCK_reg.write((byte) ((LOCK_reg.read() & 0x0f) | 0x50));
                if (radioPrinter != null) {
                    radioPrinter.println("CC1000: Calibration complete ");
                }
                calibrating = false;
            }
}
