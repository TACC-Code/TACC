package clonedetect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class InvertedIndex{
    public void creadIndex(int startIndex, int endIndex, Data data){

        Map<String, HashSet<Integer>> invertedIndex = new HashMap<>();
        for (int i = startIndex; i < endIndex; i++) {
            if(data.allFuncs.get(i) == null) {
                continue;
            }
            List<String> NGrams = data.allFuncs.get(i).nGramSequences;
            for (String nGram: NGrams){
                if (invertedIndex.containsKey(nGram)){
                    invertedIndex.get(nGram).add(i);
                } else {
                    HashSet<Integer> tmpSet = new HashSet<>();
                    tmpSet.add(i);
                    invertedIndex.put(nGram, tmpSet);
                }
            }
        }

        data.invertedIndex = invertedIndex;
    }
}
