class BackupThread extends Thread {
    protected boolean importDevice(int deviceID, String deviceName, int manufacturerID, int productID, int firmwareVersion, int channelCount, int transmitComponentCount, int receiveComponentCount, String roomName, boolean xmitsLinks) {
        boolean deviceNew = false;
        boolean deviceChanged = false;
        boolean roomChanged = false;
        UPBRoomI oldRoom = null;
        UPBDeviceI importDevice = null;
        UPBProductI importProduct = factory.lookupProduct(manufacturerID, productID);
        if ((importDevice = importNetwork.getDeviceByID(deviceID)) != null) {
            if (importDevice.getClass() != importProduct.getDeviceClass()) {
                if (UPBManager.DEBUG_MODE) debug("Changing device class for device ID " + deviceID + " from " + importDevice.getClass().getName() + " to " + importProduct.getDeviceClass().getName());
                try {
                    UPBDeviceI replacementDevice = (UPBDeviceI) importProduct.getDeviceClass().newInstance();
                    replacementDevice.setDeviceInfo(importNetwork, importProduct, deviceID);
                    replacementDevice.copyFrom(importDevice);
                    importNetwork.addDevice(replacementDevice);
                    for (UPBManager.QualifiedDeviceListener theListener : upbManager.deviceListenerList) {
                        if (theListener.theDevice == importDevice) theListener.theDevice = replacementDevice;
                    }
                    importDevice.releaseResources();
                    importDevice = replacementDevice;
                    deviceChanged = true;
                } catch (Throwable anyError) {
                    error("Unable to create tailored device instance -- " + anyError.getMessage());
                    anyError.printStackTrace();
                    return false;
                }
            }
        } else {
            try {
                importDevice = (UPBDeviceI) importProduct.getDeviceClass().newInstance();
                importDevice.setDeviceInfo(importNetwork, importProduct, deviceID);
                deviceNew = true;
                deviceChanged = true;
            } catch (Throwable anyError) {
                error("Unable to create tailored device instance -- " + anyError.getMessage());
                anyError.printStackTrace();
                return false;
            }
        }
        if (importDevice.getProductInfo() != importProduct) {
            importDevice.setDeviceInfo(importNetwork, importProduct, deviceID);
            deviceChanged = true;
        }
        if (importDevice.getFirmwareVersion() != firmwareVersion) {
            importDevice.setFirmwareVersion(firmwareVersion);
            deviceChanged = true;
        }
        if (channelCount != importDevice.getChannelCount()) {
            if (UPBManager.DEBUG_MODE) debug("Channel Count mismatch on deviceID " + deviceID + " between import file (" + channelCount + ") and device (" + importDevice.getChannelCount() + ") -- updating device");
            importDevice.setChannelCount(channelCount);
        }
        if (transmitComponentCount != importProduct.getTransmitComponentCount()) {
            if (UPBManager.DEBUG_MODE) debug("Transmit Component Count mismatch on deviceID " + deviceID + " between import file (" + transmitComponentCount + ") and product (" + importProduct.getTransmitComponentCount() + ")");
        }
        if (receiveComponentCount != importDevice.getReceiveComponentCount()) {
            if (UPBManager.DEBUG_MODE) debug("Receive Component Count mismatch on deviceID " + deviceID + " between import file (" + receiveComponentCount + ") and device (" + importDevice.getReceiveComponentCount() + ")");
            importDevice.setReceiveComponentCount(receiveComponentCount);
        }
        UPBRoomI importRoom = null;
        if ((importRoom = importNetwork.getRoomNamed(roomName)) == null) {
            importRoom = factory.createRoom(importNetwork, roomName);
            importNetwork.addRoom(importRoom);
        }
        if (importDevice.getRoom() != importRoom) {
            roomChanged = true;
            if (importDevice.getRoom() != null) {
                oldRoom = importDevice.getRoom();
                importDevice.getRoom().getDevices().remove(importDevice);
            }
            importDevice.setRoom(importRoom);
            importDevice.getRoom().getDevices().add(importDevice);
            deviceChanged = true;
        }
        if (!importDevice.getDeviceName().equals(deviceName)) {
            importDevice.setDeviceName(deviceName);
            deviceChanged = true;
        }
        if (xmitsLinks != importDevice.doesTransmitsLinks()) {
            importDevice.setTransmitsLinks(xmitsLinks);
            deviceChanged = true;
        }
        if (deviceNew) importNetwork.addDevice(importDevice);
        if (deviceChanged) deviceChangedMap[deviceID] = true;
        if (deviceChanged) markChangeFound();
        if (!deviceNew && deviceChanged) upbManager.fireDeviceEvent(new UPBDeviceEvent(importDevice, org.cdp1802.upb.UPBDeviceEvent.EventCode.DEVICE_ID_CHANGED, ALL_CHANNELS));
        if (roomChanged) {
            if (oldRoom != null) {
                upbManager.fireRoomEvent(new UPBRoomEvent(oldRoom, UPBRoomEvent.EventCode.DEVICE_REMOVED, importDevice));
            }
            upbManager.fireRoomEvent(new UPBRoomEvent(importDevice.getRoom(), UPBRoomEvent.EventCode.DEVICE_ADDED, importDevice));
        }
        return true;
    }
}
