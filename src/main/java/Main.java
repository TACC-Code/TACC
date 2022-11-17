import clonedetect.CloneDetector;
import clonedetect.Data;
import clonedetect.PreProcess;
import myutils.Func;
import myutils.JavaExtract;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


public class Main2 {
    //private static String projectPath  = "/bdata2/yyh/dataset/6-14/";
    private static String projectPath = null;
    private static int threadNum = 50;
    private static int partitionNum = 1000;
    private static int N = 3;
    private static String outputPath = "../../../data/";

    public static void main(String[] args) {
        setParameter(args);
        long startTime = System.currentTimeMillis();

        JavaExtract javaFileExtract = new JavaExtract();
        LinkedList<String> allJavaFiles= javaFileExtract.GetDirectory(projectPath);

        int files_num = allJavaFiles.size();
        //System.out.println(Runtime.getRuntime().freeMemory()/1024/1024); 
        
        System.out.println("All files num:"+files_num);


        //获取所有函数
        int divFileNum = (files_num + threadNum - 1)/threadNum;
        Data data = new Data(N, projectPath, allJavaFiles);
        PreProcess[] processes = new PreProcess[threadNum];
        final CountDownLatch latch1 = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum-1; i++) {
            processes[i] = new PreProcess(i*divFileNum, Math.min((i+1)*divFileNum, files_num), data, latch1);
            processes[i].t.start();
        }
        processes[threadNum-1] = new PreProcess((threadNum-1)*divFileNum, files_num, data, latch1);
        processes[threadNum-1].t.start();
        try {
            latch1.await();//开启门闩，阻塞所有线程，
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All functions num:"+data.allFuncs.size());

        allJavaFiles.clear();
        processes = null;

        long endTime   = System.currentTimeMillis();
        long TotalTime = endTime - startTime;
        System.out.println("Preprocess time: "+TotalTime/1000f);
        startTime = System.currentTimeMillis();

        System.out.println("使用内存"+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024); 
        System.out.println("max memory"+Runtime.getRuntime().maxMemory()/1024/1024); 
        System.out.println("total memory"+Runtime.getRuntime().totalMemory()/1024/1024); 
        System.out.println("free memory"+Runtime.getRuntime().freeMemory()/1024/1024); 


        // File functionMap = new File(outputPath + "../../../data/allFunctionMap.csv");
        // try{
        //     //第二步：通过BufferedReader类创建一个使用默认大小输出缓冲区的缓冲字符输出流
        //     BufferedWriter writeText = new BufferedWriter(new FileWriter(functionMap));

        //     //第三步：将文档的下一行数据赋值给lineData，并判断是否为空，若不为空则输出
        //     for (Func tmpFunc : data.allFuncs.values()) {
        //         writeText.write(tmpFunc.funcID+","+tmpFunc.fileName+","+tmpFunc.startLine+","+tmpFunc.endLine);
        //         writeText.newLine();    //换行
        //     }
        //     //使用缓冲区的刷新方法将数据刷到目的地中
        //     writeText.flush();
        //     //关闭缓冲区，缓冲区没有调用系统底层资源，真正调用底层资源的是FileWriter对象，缓冲区仅仅是一个提高效率的作用
        //     //因此，此处的close()方法关闭的是被缓存的流对象
        //     writeText.close();
        // }catch (FileNotFoundException e){
        //     System.out.println("Don't find allFunctionMap.csv file!");
        // }catch (IOException e){
        //     System.out.println("File write/read error!");
        // }


        long pairsNumber = 0;

        int allFuncsNum = data.allFuncs.size();
        int partitionSize = (allFuncsNum + partitionNum - 1)/partitionNum;
        //InvertedIndex indexClass = new InvertedIndex();
        for (int i = 0; i < partitionNum; i++) {
            int startIndex = i*partitionSize;
            int endIndex = Math.min(allFuncsNum, startIndex + partitionSize);
            
            Map<String, HashSet<Integer>> invertedIndex = new HashMap<>();
            for (int j = startIndex; j < endIndex; j++) {
                if(data.allFuncs.get(j) == null) {
                    continue;
                }
                List<String> NGrams = data.allFuncs.get(j).nGramSequences;
                for (String nGram: NGrams){
                    if (invertedIndex.containsKey(nGram)){
                        invertedIndex.get(nGram).add(j);
                    } else {
                        HashSet<Integer> tmpSet = new HashSet<>();
                        tmpSet.add(j);
                        invertedIndex.put(nGram, tmpSet);
                    }
                }
            }
    
            data.invertedIndex = invertedIndex;


            System.out.println("the "+i+"st inverted index size is "+data.invertedIndex.size());
            System.out.println("使用内存"+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024); 
            System.out.println("max memory"+Runtime.getRuntime().maxMemory()/1024/1024); 
            System.out.println("total memory"+Runtime.getRuntime().totalMemory()/1024/1024); 
            System.out.println("free memory"+Runtime.getRuntime().freeMemory()/1024/1024); 

            int funcNum = allFuncsNum - startIndex;
            int funcBlock = (funcNum+threadNum-1)/threadNum;
            CloneDetector[] ccThread = new CloneDetector[threadNum];
            final CountDownLatch latch2 = new CountDownLatch(threadNum);
            for (int j = 0; j < threadNum-1; j++) {
                ccThread[j] = new CloneDetector(startIndex+j*funcBlock, Math.min(startIndex+(j+1)*funcBlock, allFuncsNum), data, latch2);
                ccThread[j].t.start();
            }
            ccThread[threadNum-1] = new CloneDetector(startIndex+(threadNum-1)*funcBlock, allFuncsNum, data, latch2);
            ccThread[threadNum-1].t.start();
            try {
                latch2.await();//开启门闩，阻塞所有线程，
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("the "+i+"st clone detect done!");
            int num = 0;
            for (HashSet<Integer> v : data.clonePairs.values()) {
                num += v.size();
            }
            System.out.println("clone pairs number:"+num);
            pairsNumber += num;

        //     File writeFile = new File(outputPath+i+".csv");

        //     try{
        //         //第二步：通过BufferedReader类创建一个使用默认大小输出缓冲区的缓冲字符输出流
        //         BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));

        //         //第三步：将文档的下一行数据赋值给lineData，并判断是否为空，若不为空则输出
        //         for (Map.Entry<Integer, HashSet<Integer>> entry : data.clonePairs.entrySet()) {
        //             Func af = data.allFuncs.get(entry.getKey());
        //             for (int b: entry.getValue()) {
        //                 Func bf = data.allFuncs.get(b);
        //                 writeText.write(af.funcID+","+bf.funcID);
        //                 writeText.newLine(); 
        //             }
        //         }
        //         //使用缓冲区的刷新方法将数据刷到目的地中
        //         writeText.flush();
        //         //关闭缓冲区，缓冲区没有调用系统底层资源，真正调用底层资源的是FileWriter对象，缓冲区仅仅是一个提高效率的作用
        //         //因此，此处的close()方法关闭的是被缓存的流对象
        //         writeText.close();
        //     }catch (FileNotFoundException e){
        //         System.out.println("Don't find allFunctionMap.csv file!");
        //     }catch (IOException e){
        //         System.out.println("File write/read error!");
        //     }

            data.clonePairs = new HashMap<>();

        }

        endTime   = System.currentTimeMillis();
        TotalTime = endTime - startTime;
        System.out.println("Clone detect time: "+TotalTime/1000f);

        System.out.println("All clone pairs number is:"+pairsNumber);
        
    }

    private static void setParameter(String[] args) {
        int lens = args.length;
        if(lens %2 != 0) {
            System.out.println("parameter error! please start with '-'!");
            System.exit(0);
        }
        for (int i = 0; i < lens; i+=2) {
            String arg = args[i];
            if(arg.charAt(0) != '-'){
                System.out.println("parament error!");
                System.exit(0);
            } 
            switch(arg) {
                case "-input":
                    projectPath  = args[i+1];
                    break;
                case "-output":
                    if(args[i+1].charAt(args[i+1].length() - 1) == '/' || args[i+1].charAt(args[i+1].length() - 1) == '\\') {
                        outputPath  = args[i+1];
                    } else {
                        System.out.println("output should end with '/' or '\\'!");
                        System.exit(0);
                    }
                    break;
                case "-partition":
                    try{
                        partitionNum = Integer.parseInt(args[i+1]);
                    } catch(Exception e) {
                        System.out.println("partition is not a integer!");
                        System.exit(0);
                    }
                    break;
                case "-N":
                    try{
                        N = Integer.parseInt(args[i+1]);
                    } catch(Exception e) {
                        System.out.println("N is not a integer!");
                        System.exit(0);
                    }
                    break;
                case "-thread":
                    try{
                        threadNum = Integer.parseInt(args[i+1]);
                    } catch(Exception e) {
                        System.out.println("thread is not a integer!");
                        System.exit(0);
                    }
                    break;
                case "-fs":
                    try{
                        Data.filter_score = Float.parseFloat(args[i+1]);
                    } catch(Exception e) {
                        System.out.println("filter score is not a float!");
                        System.exit(0);
                    }
                    break;
                case "-tvs":
                    try{
                        Data.verify_score = Float.parseFloat(args[i+1]);
                    } catch(Exception e) {
                        System.out.println("token verify score is not a float!");
                        System.exit(0);
                    }
                    break;
                case "-nnavs":
                    try{
                        Data.notNeedASTVerify_score = Float.parseFloat(args[i+1]);
                    } catch(Exception e) {
                        System.out.println("not need ast verify score is not a float!");
                        System.exit(0);
                    }
                    break;
                case "-atvs":
                    try{
                        Data.astTwo_score = Float.parseFloat(args[i+1]);
                    } catch(Exception e) {
                        System.out.println("ast two verify score is not a float!");
                        System.exit(0);
                    }
                    break;
                case "-afvs":
                    try{
                        Data.astFour_score = Float.parseFloat(args[i+1]);
                    } catch(Exception e) {
                        System.out.println("ast four verify score is not a float!");
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("unkown parameter");
                    System.exit(0);
            }
        }
        if (projectPath == null) {
            System.out.println("Project path is null!");
            System.exit(0);
        }
    }
}
