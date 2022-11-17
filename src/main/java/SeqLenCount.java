import clonedetect.Data;
import clonedetect.PreProcess;
import myutils.Func;
import myutils.JavaExtract;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class SeqLenCount {
    private static String projectPath  = "/bdata/wyk/dataset/dataset/data10M/IJaDataset10M/";
    private static int preprocessThreadNum = 50;
    private static int N = 5;
    public static void main(String[] args) {
        JavaExtract javaFileExtract = new JavaExtract();
        LinkedList<String> allJavaFiles= javaFileExtract.GetDirectory(projectPath);
        int files_num = allJavaFiles.size();
        System.out.println("file num:"+ files_num);

        int blocks = files_num/preprocessThreadNum;
        Data data = new Data(N, projectPath, allJavaFiles);
        PreProcess[] processes = new PreProcess[preprocessThreadNum];
        final CountDownLatch latch1 = new CountDownLatch(preprocessThreadNum);
        for (int i = 0; i < preprocessThreadNum-1; i++) {
            processes[i] = new PreProcess(i*blocks, Math.min((i+1)*blocks, files_num), data, latch1);
            processes[i].t.start();
        }
        processes[preprocessThreadNum-1] = new PreProcess((preprocessThreadNum-1)*blocks, files_num, data, latch1);
        processes[preprocessThreadNum-1].t.start();
        try {
            latch1.await();//开启门闩，阻塞所有线程，
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
 
        int funcNum = data.allFuncs.size();
        System.out.println("funcNum:"+funcNum);
        Map<Integer, Integer> tokenCnt = new HashMap<>();
        Map<Integer, Integer> nodeCnt = new HashMap<>();
        for (int i = 0; i < funcNum; i++) {
            Func tmpFunc = data.allFuncs.get(i);
            int tokensNum = tmpFunc.divTokenSequence.size();
            int astNodeNum = tmpFunc.astNodeNum;
            if(tokensNum > 50) {
                tokenCnt.put(tokensNum/100, tokenCnt.getOrDefault(tokensNum/100, 0)+1);
                if(astNodeNum > 5) {
                    nodeCnt.put(astNodeNum/100, nodeCnt.getOrDefault(astNodeNum/100, 0)+1);
                }
            }
        }
        System.out.println(tokenCnt);

        File writeFile = new File("/bdata2/yyh/LVMapper_Java/data/tokens10M.csv");

        try{
            BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));
            for (Map.Entry<Integer, Integer> entry : tokenCnt.entrySet()) {
                String tempStr = entry.getKey()*100+"--"+((entry.getKey()+1)*100-1)+","+entry.getValue();
                //System.out.println(tempStr);
                writeText.write(tempStr);
                writeText.newLine();
            }
            writeText.flush();
            writeText.close();
        }catch (FileNotFoundException e){
            System.out.println("没有找到指定文件");
        }catch (IOException e){
            System.out.println("文件读写出错");
        }

        writeFile = new File("/bdata2/yyh/LVMapper_Java/data/ast10M.csv");

        try{
            BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));
            for (Map.Entry<Integer, Integer> entry : nodeCnt.entrySet()) {
                writeText.write(entry.getKey()*100+"--"+((entry.getKey()+1)*100-1)+","+entry.getValue());
                writeText.newLine();
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
