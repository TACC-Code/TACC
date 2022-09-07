class BackupThread extends Thread {
    private void loadBuildByString(String input) {
        String header, power, power_id, name;
        int header_length, build_length, power_length, name_length, activated_powers_length, build_count, trail_count, length_digits;
        BuildPower build_power;
        PowerData a;
        try {
            header_length = Integer.valueOf(input.substring(0, 1), 36);
            header = input.substring(1, header_length);
            length_digits = Integer.valueOf(header.substring(0, 1), 36);
            build_count = Integer.valueOf(header.substring(1, 2), 36);
            trail_count = Integer.valueOf(header.substring(2, 3), 36);
            input = input.substring(header_length);
            for (int i = 0; i < build_count; i++) {
                build_length = Integer.valueOf(input.substring(0, length_digits), 36);
                input = input.substring(length_digits);
                for (int j = 0; j < build_length; j++) {
                    power_length = Integer.valueOf(input.substring(0, length_digits), 36);
                    power = input.substring(1, power_length);
                    power_id = power.substring(0, 4);
                    input = input.substring(power_length);
                    try {
                        build_power = build_lists.get(i).getSlots().get(j);
                        if (!power_id.equals("zzzz")) {
                            try {
                                build_power.setPower(power_data_map.get(power_id));
                                if (power_length > 5) {
                                    for (String a_id : advantage_map.get(power_id).keySet()) {
                                        a = advantage_map.get(power_id).get(a_id);
                                        if (power.substring(4).contains(a_id)) {
                                            build_power.addAdvantage(a);
                                            a.setUsed(true);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                sys_link.writeError("reading power from file", e);
                                e.printStackTrace();
                                user_interface.setTopText(sys_link.translate("error_unknown"), 0);
                            }
                        }
                    } catch (Exception e) {
                        sys_link.writeError("reading build from file", e);
                        e.printStackTrace();
                        user_interface.setTopText(sys_link.translate("error_parse"), 0);
                    }
                }
            }
            if (trail_count > 0) {
                name_length = Integer.valueOf(input.substring(0, length_digits), 36);
                name = input.substring(1, name_length);
                char_sheet.setCharacterName(name);
                input = input.substring(name_length);
                if (trail_count > 1) {
                    activated_powers_length = Integer.valueOf(input.substring(0, length_digits), 36);
                    for (int i = 1; i < activated_powers_length; i++) {
                        try {
                            build_power = build_lists.getFirst().getSlots().get(i - 1);
                            if (!build_power.isEmpty()) build_power.setPowerActivated(input.substring(i, i + 1).equals("1"));
                        } catch (Exception e) {
                            sys_link.writeError("reading build from file", e);
                            e.printStackTrace();
                            user_interface.setTopText(sys_link.translate("error_parse"), 0);
                        }
                    }
                    if (trail_count > 2) user_interface.setTopText(sys_link.translate("error_parse"), 0);
                }
            }
        } catch (Exception e) {
            sys_link.writeError("reading main load", e);
            e.printStackTrace();
            user_interface.setTopText(sys_link.translate("error_corrupted"), 0);
            clearBuild();
        }
        frameworks_active = false;
        updateAdvantages();
        enforceFilter();
        checkBuild();
        updateCharacteristics();
    }
}
