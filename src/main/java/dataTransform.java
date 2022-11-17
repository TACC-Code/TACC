import myutils.FuncForTreeDiff;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class dataTransform {
    private static Map<Integer, FuncForTreeDiff> allFunctions = new HashMap<>();
    private static Set<Integer> parseError = new HashSet<>();
    public static void main(String[] args) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("/bdata2/yyh/LVMapper_Java/allFunctionMap.csv"))) {
            String DELIMITER = ",";
            String line;
            while ((line = br.readLine()) != null) {
                // 分割
                FuncForTreeDiff tmpFunc = new FuncForTreeDiff();
                //System.out.println(line);
                String[] columns = line.split(DELIMITER);
                try{
                    tmpFunc.funcID = Integer.parseInt(columns[0]);
                    tmpFunc.filePath = columns[1];
                    tmpFunc.startLine = Integer.parseInt(columns[2]);
                    tmpFunc.endLine = Integer.parseInt(columns[3]);
                    if (columns[1].endsWith(".java")) {
                        allFunctions.put(tmpFunc.funcID, tmpFunc);
                    } else {
                        parseError.add(tmpFunc.funcID);
                    }
                    
                } catch (Exception e){
                    int len = columns.length;
                    tmpFunc.funcID = Integer.parseInt(columns[0]);
                    String filePath = columns[1];
                    for (int i = 2; i < len-2; i++) {
                        filePath += columns[i];
                    }
                    tmpFunc.filePath = filePath;
                    System.out.println("special:"+filePath);
                    tmpFunc.startLine = Integer.parseInt(columns[len-2]);
                    tmpFunc.endLine = Integer.parseInt(columns[len-1]);
                    if (filePath.endsWith(".java")) {
                        allFunctions.put(tmpFunc.funcID, tmpFunc);
                    } else {
                        parseError.add(tmpFunc.funcID);
                    }
                }
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        File csvFileDir = new File("/bdata2/yyh/LVMapper_Java/recall_result2/");
        File[] csvFiles = csvFileDir.listFiles();

        File functionMap = new File("/bdata2/yyh/LVMapper_Java/unionReacll.csv");
        try{
            BufferedWriter writeText = new BufferedWriter(new FileWriter(functionMap));

                

                for(File tmpFile : csvFiles) {
                    try (BufferedReader br = Files.newBufferedReader(Paths.get(tmpFile.getAbsolutePath()))) {
                        String DELIMITER = ",";
                        String line = br.readLine();
                        while ((line = br.readLine()) != null) {
                            // 分割
                            String[] columns = line.split(DELIMITER);
                            int aFuncIndex = Integer.parseInt(columns[0]);
                            int bFuncIndex = Integer.parseInt(columns[1]);
                            if(parseError.contains(aFuncIndex) || parseError.contains(bFuncIndex)) {
                                continue;
                            }
                            FuncForTreeDiff afunc = allFunctions.get(aFuncIndex);
                            FuncForTreeDiff bfunc = allFunctions.get(bFuncIndex);
                            int aLine = afunc.endLine - afunc.startLine + 1;
                            int bLine = bfunc.endLine - bfunc.startLine + 1;
                            
                            String aProject = afunc.filePath.split("/")[6];
                            String bProject = bfunc.filePath.split("/")[6];
                            
                            writeText.write(aFuncIndex+","+aLine+","+aProject+","+bFuncIndex+","+bLine+","+bProject);
                            writeText.newLine(); 
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        
            writeText.flush();
            writeText.close();
        }catch (FileNotFoundException e){
            System.out.println("没有找到指定文件");
        }catch (IOException e){
            System.out.println("文件读写出错");
        }


        
        
        

        


    }





}
