class BackupThread extends Thread {
    public boolean hasUserAccessTo(String userName, String accessType, String functionPath, String parameterName, String parameterValue) throws Exception {
        try {
            if (functionPath == null) throw new Exception("Specified functionName cannot be null or empty.");
            if (functionPath.length() == 0) throw new Exception("Specified functionName cannot be null or empty.");
            if (accessType == null) throw new Exception("Specified access cannot be null or empty.");
            if (!accessType.equals("read") && !accessType.equals("write")) throw new Exception("Specified access can be 'read' or 'write', but not '" + accessType + "'");
            List<String> userGroups = retrieveUserGroups(userName);
            if (userGroups.size() == 0) return false;
            boolean hasUserAccess = false;
            String[] functionName = functionPath.split("/");
            int nn = functionName.length;
            AccessCheckerFunction function = _functions;
            for (int n = 0; n < nn; n++) {
                if (!function.tree.containsKey(functionName[n])) return hasUserAccess;
                function = function.tree.get(functionName[n]);
                hasUserAccess = function.hasUserAccess(hasUserAccess, accessType, userGroups);
            }
            if (parameterName != null) {
                if (parameterValue == null) throw new Exception("If parameterName is not null, then parameterValue cannot be null or empty.");
                if (!function.parameters.containsKey(parameterName)) return hasUserAccess;
                AccessCheckerParameter parameter = function.parameters.get(parameterName);
                hasUserAccess = parameter.hasUserAccess(hasUserAccess, accessType, userGroups);
                if (!parameter.values.containsKey(parameterValue)) return hasUserAccess;
                AccessCheckerElement value = parameter.values.get(parameterValue);
                hasUserAccess = value.hasUserAccess(hasUserAccess, accessType, userGroups);
            }
            return hasUserAccess;
        } catch (Exception e) {
            throw new Exception(ParseError.parseError("AccessChecker.hasUserAccessTo('" + userName + "','" + functionPath + "','" + parameterName + "','" + parameterValue + "')", e));
        }
    }
}
