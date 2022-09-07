class BackupThread extends Thread {
    public void testToString() throws Exception {
        TestUtil.printTitle("ServerModeFlagTest:testToString()");
        assertNotNull("Shouldn't be null.", ServerModeFlag.toString(ServerModeFlag.NONE));
        assertNotNull("Shouldn't be null.", ServerModeFlag.toString(ServerModeFlag.CAN_READ));
        assertNotNull("Shouldn't be null.", ServerModeFlag.toString(ServerModeFlag.CAN_READ_WRITE));
        assertNotNull("Shouldn't be null.", ServerModeFlag.toString(ServerModeFlag.CAN_WRITE));
        System.out.println("Should say something about \"none\": " + ServerModeFlag.toString(ServerModeFlag.NONE));
        System.out.println("Should say something about \"can read\": " + ServerModeFlag.toString(ServerModeFlag.CAN_READ));
        System.out.println("Should say something about \"can read & write\": " + ServerModeFlag.toString(ServerModeFlag.CAN_READ_WRITE));
        System.out.println("Should say something about \"can write\": " + ServerModeFlag.toString(ServerModeFlag.CAN_WRITE));
        try {
            byte fake = Byte.MAX_VALUE;
            String str = "Should have thrown an  exception for flag " + fake + ", instead: " + ServerModeFlag.toString(fake);
            fail(str);
        } catch (Exception nope) {
        }
    }
}
