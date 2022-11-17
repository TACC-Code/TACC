from operator import imod
import os
import sys
import javalang
import pickle

def get_similarity(block1_path, block2_path):
    block1 = getCodeBlock(block1_path)
    block2 = getCodeBlock(block2_path)

    if(len(block1) > len(block2)):
        temp = block1
        block1 = block2
        block2 = temp  
    line1 = 0
    common_lines = 0
    lastline = -1
    block1_len = len(block1)
    block2_len = len(block2)
    while(line1 < block1_len):
        k1 = block1[line1]     #原文是要获取其hash值的，节省内存，但这里求两个块的相似度就不用了
        line2 = findInBlock(k1, block2_len, block2, lastline)
        if(line2 != -1):
            seglen = 1
            while(line1+seglen < block1_len and line2+seglen< block2_len 
                    and block1[line1+seglen] == block2[line2+seglen]):
                seglen += 1
            if (seglen >= 2):
                common_lines += seglen
                lastline = line2 + seglen - 1
                line1 += seglen
                continue
        line1 += 1
    # file = open('size/t3.txt','ab')
    # pickle.dump((block1,block2),file)
    # file.close()
    # size_a = sys.getsizeof(block1)
    # size_b = sys.getsizeof(block2)
    # size = size_a + size_b             
    return common_lines/block1_len#, size         

def getCodeBlock(file_path):
    block = []
    # print(file_path)
    with open(file_path, 'r') as temp_file:
        lines = temp_file.readlines()
        for line in lines:
            tokens = list(javalang.tokenizer.tokenize(line))
            temp_line = ''   #把一行的所有元素连接后当做一个token
            for token in tokens:
                if (type(token) == javalang.tokenizer.Identifier):
                    temp_line += 'id'
                else:
                    temp_line += token.value
            block.append(temp_line)

    return block

def get_filter_similarity(block1_path, block2_path, N):
    '''与nil仅有最后一行不同'''
    block1 = getCodeBlock(block1_path)
    block2 = getCodeBlock(block2_path)
    common_ngram = 0
    invertedIndex = {} #记录包含ngram的block下标
    for j in range(len(block1) - N + 1):
        n_gram = ''
        for k in range(N):
            n_gram += block1[j + k]
        n_gram_key = hash(n_gram)
        if(n_gram_key in invertedIndex):
            invertedIndex[n_gram_key].append(1)
        else:
            invertedIndex[n_gram_key] = [1]
    for j in range(len(block2) - N + 1):
        n_gram = ''
        for k in range(N):
            n_gram += block2[j + k]
        n_gram_key = hash(n_gram)
        if(n_gram_key in invertedIndex):
            invertedIndex[n_gram_key].append(2)
        else:
            invertedIndex[n_gram_key] = [2]    

    for temp_ls in invertedIndex.values():
        common_ngram += min(temp_ls.count(1), temp_ls.count(2))  
    
    return common_ngram/(max(len(block1), len(block2)) - N + 1)

def findInBlock(k1, block2_len, block2, lastline):
    start = lastline+1
    while(start < block2_len):
        if (k1 == block2[start]):
            return start
        start += 1
    return -1

def LVMapper_Similarity(f1,f2):
    return get_similarity(f1, f2)
