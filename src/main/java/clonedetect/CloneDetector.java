package clonedetect;
import myutils.Func;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class CloneDetector extends Thread{

    private Data data;
    public Thread t;
    int start_func;
    int end_func;
    private final CountDownLatch latch;
    //private Map<Integer, Map<Integer, Double>> edgeScores = new HashMap<>();

    public CloneDetector(int start_func, int end_func, Data data, CountDownLatch latch){
        this.start_func = start_func;
        this.end_func = end_func;
        this.data = data;
        this.latch = latch;
        t = new Thread(this, "pre"+start_func);
    }

    public HashSet<Integer> getClonePair(int funcID){
        Func tmpFunc = data.allFuncs.get(funcID);
        //locate phase
        HashSet<Integer> cloneCandidate = locatePhase(tmpFunc);
        if(data.allFuncs.getOrDefault(funcID, null) == null) {
            return cloneCandidate;
        }
        HashSet<Integer> notNeedASTVerify = new HashSet<>();
        //filter phase
        HashSet<Integer> removeEle = NLineFilter(cloneCandidate, funcID, notNeedASTVerify);
        cloneCandidate.removeAll(removeEle);
        cloneCandidate.removeAll(notNeedASTVerify);
        //verify phase, 在main函数中的GPT生成后 克隆检测前要对data中的div进行排序
        removeEle = sourceCCVerify(cloneCandidate, funcID, notNeedASTVerify);
        cloneCandidate.removeAll(removeEle);
        cloneCandidate.removeAll(notNeedASTVerify);
        removeEle = astTwoVerify(cloneCandidate, funcID);
        HashSet<Integer> set2 = astFourVerify(cloneCandidate, funcID);
        removeEle.retainAll(set2);
        cloneCandidate.removeAll(removeEle);
        cloneCandidate.addAll(notNeedASTVerify);
        return cloneCandidate;
    }

    public HashSet<Integer> locatePhase(Func func) {
        HashSet<Integer> cc = new HashSet<>();
        if(func == null || func.nGramSequences == null) {
            return cc;
        }
        for (String ngramHash: func.nGramSequences) {
            if (data.invertedIndex.containsKey(ngramHash)) {
                cc.addAll(data.invertedIndex.get(ngramHash));
            }
        }
        return cc;
    }

    public HashSet<Integer> NLineFilter(HashSet<Integer> cloneCandidate, int c_block_index, HashSet<Integer> notNeedASTVerify) {
        HashSet<Integer> remove_ele = new HashSet<>();
        for (int b_block_index : cloneCandidate) {
            if (b_block_index >= c_block_index) {
                remove_ele.add(b_block_index);
                continue;
            }
            Func b_block = data.allFuncs.get(b_block_index);
            Func c_block = data.allFuncs.get(c_block_index);
            int common_ngram = getCommonNGram(b_block_index, c_block.nGramSequences);
            int max_len = Math.max(c_block.funcLen, b_block.funcLen) - data.N + 1;
            //若只考虑长度大于6的代码，这步可以省略
//            if (min_len == 0) {
//                min_len = 10;
//            }
            float filterSim = 1.0f*common_ngram/max_len;
            if (filterSim < Data.filter_score) {
                remove_ele.add(b_block_index);
            } else if(filterSim > Data.notNeedASTVerify_score) {
                notNeedASTVerify.add(b_block_index);
            }
        }
        return remove_ele;
    }

    public HashSet<Integer> lvMapperFilter(List<Integer> cloneCandidate) {
        Collections.sort(cloneCandidate);
        HashSet<Integer> remove_element = new HashSet<>();
        int b_funcID = cloneCandidate.get(0), b_cnt = 1;
        for(int i=1; i<cloneCandidate.size(); i++) {
            if(cloneCandidate.get(i) == b_funcID) {
                b_cnt++;
                continue;
            }
            float filter_score = 1.0f*b_cnt/(data.allFuncs.get(b_funcID).endLine -
                    data.allFuncs.get(b_funcID).startLine - data.N + 1);
            if (filter_score < Data.filter_score) {
                remove_element.add(b_funcID);
            }
            b_funcID = cloneCandidate.get(i);
            b_cnt = 1;
        }
        float filter_score = 1.0f*b_cnt/(data.allFuncs.get(b_funcID).endLine -
                data.allFuncs.get(b_funcID).startLine - data.N + 1);
        if (filter_score < Data.filter_score) {
            remove_element.add(b_funcID);
        }
        return remove_element;
    }

    public HashSet<Integer> sourceCCVerify(HashSet<Integer> cloneCandidate, int c_block_index, HashSet<Integer> notNeedASTVerify) {
        HashSet<Integer> remove_ele = new HashSet<>();
        for (int b_block_index : cloneCandidate) {

            List<Integer> b_block = data.allFuncs.get(b_block_index).divTokenSequence;
            List<Integer> c_block = data.allFuncs.get(c_block_index).divTokenSequence;

            int ct = (int)Math.ceil(Math.max(b_block.size(), c_block.size())*Data.verify_score);
            //大于0.7不需要进一步验证
            int mt = (int)Math.ceil(Math.max(b_block.size(), c_block.size())*0.7);
            int overlapTokenNum = overlapNum(b_block, c_block);
            if (overlapTokenNum < ct) {
                remove_ele.add(b_block_index);
            } else if (overlapTokenNum > mt) {
                notNeedASTVerify.add(b_block_index);
            }
        }
        return remove_ele;
    }

    private HashSet<Integer> astFourVerify(HashSet<Integer> cloneCandidate, int c_block_index) {
        HashSet<Integer> removeEle = new HashSet<>();
        Func cFunc = data.allFuncs.get(c_block_index);
        // if (cFunc.astNotVisted) {
        //     cFunc.astNotVisted = false;
        //     setFunctionASTRepresentation(c_block_index);
        // }
        for (int b_block_index : cloneCandidate) {
            Func bFunc = data.allFuncs.get(b_block_index);
            // if (bFunc.astNotVisted) {
            //     bFunc.astNotVisted = false;
            //     setFunctionASTRepresentation(b_block_index);
            // }
            Double sc = calJaccard(bFunc.astNodeAllType, cFunc.astNodeAllType);
            if (sc < Data.astFour_score) { 
                removeEle.add(b_block_index);
            }
        }
        return removeEle;
    }

    private double calJaccard(List<Short> v1, List<Short> v2) {
        //暂未发现原因
        if (v1.size() == 0 || v2.size() == 0) {
            return 0;
        }
        int son = 0, par = 0;
        for (int i = 0; i <= 75; i++) {
            son += Math.abs(v1.get(i) - v2.get(i));
            par += Math.max(v1.get(i), v2.get(i));
        }
        return 1 - 1.0*son/par;
    }
    private double calJaccardNum(int[] v1, int[] v2) {
        int son = 0, par = 0;
        for (int i = 0; i < Data.edgeTypeNum; i++) {
            son += Math.abs(v1[i] - v2[i]);
            par += Math.max(v1[i],  v2[i]);
        }
        return 1 - 1.0*son/par;
    }



    private HashSet<Integer> astTwoVerify(HashSet<Integer> cloneCandidate, int c_block_index) {
        HashSet<Integer> removeEle = new HashSet<>();
        Func cFunc = data.allFuncs.get(c_block_index);
        // if (cFunc.astNotVisted) {
        //     cFunc.astNotVisted = false;
        //     setFunctionASTRepresentation(c_block_index);
        // }
        Map<Integer, Short> hashCnt1 = cFunc.astNodeHashCnt;
        int cAstNodeNum = cFunc.astNodeNum;
        for (int b_block_index : cloneCandidate) {
            Func bFunc = data.allFuncs.get(b_block_index);
            // if (bFunc.astNotVisted) {
            //     bFunc.astNotVisted = false;
            //     setFunctionASTRepresentation(b_block_index);
            // }
            Map<Integer, Short> hashCnt2 = bFunc.astNodeHashCnt;
            int bAstNodeNum = bFunc.astNodeNum;
            int overlap = 0;
            for (Map.Entry<Integer, Short> entry: hashCnt1.entrySet()){
                if (hashCnt2.containsKey(entry.getKey())) {
                    overlap += Math.min(entry.getValue(), hashCnt2.get(entry.getKey()));
                }
            }
            double score = 1.0*overlap/Math.max(cAstNodeNum, bAstNodeNum);
            if (score < Data.astTwo_score) {
                removeEle.add(b_block_index);
            }
        }
        return removeEle;
    }



    public int overlapNum(List<Integer> ls1, List<Integer> ls2) {
        int res = 0;
        int len1 = ls1.size(), len2 = ls2.size();
        int i1=0, i2=0;
        while (i1<len1 && i2<len2) {
            int tmp1 = ls1.get(i1), tmp2 = ls2.get(i2);
            if (tmp1 == tmp2){
                res++;
                i1++;
                i2++;
            } else {
                if (tmp1 < tmp2) {
                    i1++;
                } else {
                    i2++;
                }
            }
        }
        return res;
    }

    //考虑c中的重复元素，但不考虑b中的
    public int getCommonNGram(int b_block_index, List<String> c_ngram){
        int res = 0;
        for (String ngramHash : c_ngram) {
            if (data.invertedIndex.containsKey(ngramHash) && data.invertedIndex.get(ngramHash).contains(b_block_index)) {
                res++;
            }
        }
        return res;
    }



    @Override
    public void run() {
        //HashMap<Integer, HashSet<Integer>> tmpClonePairs = new HashMap<>();
        for(int i=start_func; i < end_func; i++) {
            HashSet<Integer> tmpSet = getClonePair(i);
            if (tmpSet.size() > 0) {
                if(data.clonePairs.containsKey(i)) {
                    data.clonePairs.get(i).addAll(tmpSet);
                }
                else {
                    data.clonePairs.put(i, tmpSet);
                }
            }
            // if(i%100000 == 0) {
            //     System.out.println("func:"+i+" done!");
            //     System.out.println("free memory"+Runtime.getRuntime().freeMemory()/1024/1024); 
            // }
        }
        //System.out.println("start:"+start_func+" to end:"+end_func+" done!");
        latch.countDown();
    }


}
