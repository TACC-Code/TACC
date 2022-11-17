import sys
import javalang
import os
import pickle
__GloPT__ = dict()


def generate_GPT(code_blocks) :
    #这里只有两个block
    for block in code_blocks:
        for token in block:
            if (__GloPT__.__contains__(token)) :
                __GloPT__[token] = __GloPT__[token] + 1
            else:
                __GloPT__[token] = 1

def sortBlockWithGPT(block) :
    # 根据token出现次数从小到大对block进行排序
    block.sort(key=lambda item: __GloPT__[item])

def getCodeBlock(file_path):
    block = []
    # print(file_path)
    with open(file_path, 'r') as temp_file:
        lines = temp_file.readlines()
        for line in lines:
            tokens = list(javalang.tokenizer.tokenize(line))
            for token in tokens:
                block.append(token.value)
    return block

def overlapSimilarity(ls_1, ls_2):
    res = 0
    len1, len2 = len(ls_1), len(ls_2)
    i_1, i_2 = 0, 0
    while (i_1 < len1 and i_2 < len2) :
        if (ls_1[i_1] == ls_2[i_2]) :
            res += 1
            i_1 += 1
            i_2 += 1
        else :
            if (__GloPT__[ls_1[i_1]] < __GloPT__[ls_2[i_2]]) :
                i_1 += 1
            elif (__GloPT__[ls_1[i_1]] > __GloPT__[ls_2[i_2]]):
                i_2 += 1
            else :
                if(ls_1[i_1] < ls_2[i_2]):
                    i_1 += 1
                else :
                    i_2 += 1
    return res

def get_similarity(block1_path, block2_path):
    block1 = getCodeBlock(block1_path)
    block2 = getCodeBlock(block2_path)
    generate_GPT([block1, block2])
    sortBlockWithGPT(block1)
    sortBlockWithGPT(block2)
    lcs_len = overlapSimilarity(block1, block2)
    size_a = sys.getsizeof(block1)
    size_b = sys.getsizeof(block2)
    # file = open('size/t2.txt','ab')
    # pickle.dump((block1,block2),file)
    # file.close()
    # size = size_a + size_b
    return lcs_len / max(len(block1), len(block2))#, size

def SourcererCC_Similarity(f1,f2):
    return get_similarity(f1, f2)