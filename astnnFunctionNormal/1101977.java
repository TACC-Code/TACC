class BackupThread extends Thread {
    public void establishAccessToUsersOrGroups(String action, String access, String functionPath, String usersOrGroups, String parameterName, String parameterValue) throws Exception {
        try {
            if (action == null) action = "";
            if (!action.equals("grant") && !action.equals("deny")) throw new Exception("Specified action can be 'grant' or 'deny' but not '" + action + "'");
            if (access == null) access = "";
            if (!access.equals("read") && !access.equals("write")) throw new Exception("Specified access can be 'read' or 'write' but not '" + access + "'");
            if (functionPath == null) functionPath = "";
            if (functionPath.length() == 0) throw new Exception("Specified 'functionPath' cannot be null or empty.");
            if (usersOrGroups == null) usersOrGroups = "";
            if (usersOrGroups.length() == 0) throw new Exception("Specified list of users cannot be null or empty.");
            String[] userOrGroup = usersOrGroups.split(",");
            int nn = userOrGroup.length;
            for (int n = 0; n < nn; n++) {
                if (_users.containsKey(userOrGroup[n])) continue;
                if (_groups.containsKey(userOrGroup[n])) continue;
                throw new Exception("Specified user or group '" + userOrGroup[n] + "' is neither a declared user or a declared group.");
            }
            String[] functionName = functionPath.split("/");
            nn = functionName.length;
            AccessCheckerFunction function = _functions;
            for (int n = 0; n < nn; n++) {
                if (!function.tree.containsKey(functionName[n])) {
                    AccessCheckerFunction childFunction = new AccessCheckerFunction();
                    childFunction.functionName = functionName[n];
                    function.tree.put(functionName[n], childFunction);
                }
                function = function.tree.get(functionName[n]);
            }
            AccessCheckerElement element;
            if (parameterName != null) {
                AccessCheckerParameter parameter;
                if (!function.parameters.containsKey(parameterName)) {
                    parameter = new AccessCheckerParameter();
                    parameter.parameterName = parameterName;
                    function.parameters.put(parameterName, parameter);
                } else parameter = function.parameters.get(parameterName);
                if (!parameter.values.containsKey(parameterValue)) {
                    element = new AccessCheckerElement();
                    parameter.values.put(parameterValue, element);
                } else element = parameter.values.get(parameterValue);
            } else element = function;
            List<String> accessList = null;
            if (action.equals("grant")) {
                if (access.equals("write")) {
                    accessList = element.grantWriting;
                } else if (access.equals("read")) {
                    accessList = element.grantReading;
                }
            } else if (action.equals("deny")) {
                if (access.equals("write")) {
                    accessList = element.denyWriting;
                } else if (access.equals("read")) {
                    accessList = element.denyReading;
                }
            }
            if (accessList == null) throw new Exception("Combination of action='" + action + "' and access='" + access + "' didn't determine an access list.");
            nn = userOrGroup.length;
            for (int n = 0; n < nn; n++) {
                if (!accessList.contains(userOrGroup[n])) accessList.add(userOrGroup[n]);
            }
        } catch (Exception e) {
            throw new Exception(ParseError.parseError("AccessChecker.establishUserOrGroupAccess('" + action + "','" + functionPath + "','" + usersOrGroups + "','" + parameterName + "','" + parameterValue + "')", e));
        }
    }
}
