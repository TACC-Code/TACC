package myutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author 83477
 */
public class FunctionExtractor {
    public Map<Integer, Func> loadFile(String filePath) {
        Runtime runtime = Runtime.getRuntime();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            runtime.exec("txl -q "+filePath+" ../txl/java-extract-functions.txl").getInputStream(),
                            "UTF-8"
                    )
            );
            String line;
            Map<Integer, Func> funcs = new HashMap<>();
            String funcName = "";
            short sl = 0;
            short el = 0;
            List<String> tmpLs = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if(line.startsWith("<source file=")) {
                    String[] ss = line.split(" ");
                    funcName = ss[1].substring(6, ss[1].length()-1);
                    sl = (short)Integer.parseInt(ss[2].substring(11, ss[2].length()-1));
                    el = (short)Integer.parseInt(ss[3].substring(9, ss[3].length()-2));
                    tmpLs = new ArrayList<>();
                } else if (line.startsWith("</source>")) {
                    //过滤长度小于6的函数
                    if (tmpLs.size() >= 6) {
                        Func tmpFunc = new Func();
                        tmpFunc.fileName = funcName;
                        tmpFunc.startLine = sl;
                        tmpFunc.endLine = el;
                        tmpFunc.tokenSequence = tmpLs;
                        tmpFunc.funcLen = (short)(el - sl);
                        funcs.put(tmpFunc.funcID, tmpFunc);
                    }

                } else {
                    tmpLs.add(line);
                }
            }
            return funcs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, Func> testLoadFile(String filePath) {
        Path path = Paths.get(filePath);
        List<String> allLines = new ArrayList<>();
        try {
            allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Integer, Func> funcs = new HashMap<>();
        String funcName = "";
        short sl = 0;
        short el = 0;
        List<String> tmpLs = new ArrayList<>();
        for(String line: allLines) {
            if(line.startsWith("<source file=")) {
                String[] ss = line.split(" ");
                funcName = ss[1].substring(6, ss[1].length()-1);
                sl = (short)Integer.parseInt(ss[2].substring(11, ss[2].length()-1));
                el = (short)Integer.parseInt(ss[3].substring(9, ss[3].length()-2));
                tmpLs = new ArrayList<>();
            } else if (line.startsWith("</source>")) {
                //过滤长度小于6的函数
                if (tmpLs.size() >= 6) {
                    Func tmpFunc = new Func();
                    tmpFunc.fileName = funcName;
                    tmpFunc.startLine = sl;
                    tmpFunc.endLine = el;
                    tmpFunc.tokenSequence = tmpLs;
                    funcs.put(tmpFunc.funcID, tmpFunc);
                }

            } else {
                tmpLs.add(line);
            }
        }
        return funcs;
    }
}
