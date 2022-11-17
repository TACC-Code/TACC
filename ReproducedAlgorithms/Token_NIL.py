import os
import sys
import javalang
import pickle

def getCodeBlock(file_path):
    block = []
    # print(file_path)
    with open(file_path, 'r') as temp_file:
        lines = temp_file.readlines()
        for line in lines:
            tokens = list(javalang.tokenizer.tokenize(line))
            for token in tokens:
                if (type(token) != javalang.tokenizer.Separator and type(token) != javalang.tokenizer.Operator):
                    block.append(token.value)
    return block

def get_similarity(block1_path, block2_path):
    '''return the two list's similarity'''
    block1 = getCodeBlock(block1_path)
    block2 = getCodeBlock(block2_path)
    LCS_len = similarity_Hunt_and_Szymanski(block1, block2)
    # filter_similarity = get_filter_similarity(block1, block2, 5)
    # size_a = sys.getsizeof(block1)
    # size_b = sys.getsizeof(block2)
    # size = size_a + size_b
    # file = open('size/t4.txt','ab')
    # pickle.dump((block1,block2),file)
    # file.close()
    return LCS_len/min(len(block1), len(block2))#, #filter_similarity,size

def get_filter_similarity(block1, block2, N):
    '''return the filter score through the common ngram
        原来这个是用来过滤的，这里改成计算相似度，所以也变化了一些
    '''

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
    
    return common_ngram/(min(len(block1), len(block2)) - N + 1)


def similarity_Hunt_and_Szymanski(s1, s2):
    """Return the similarity between two strings,
    i.e., the maximal number of characters in the same order in the two strings
    Algorithm: [Hunt and Szymanski, 1977] in O((|d| + log(r)) x log(min(|s1|,|s2|)))
    where d is the number of different symbols in the longest string
    and r is the number of positions with the same symbol in the two strings (equality points)

    >>> similarity_Hunt_and_Szymanski('','abcd')
    0
    >>> similarity_Hunt_and_Szymanski('abcd','abcd')
    4
    >>> similarity_Hunt_and_Szymanski('abcd','wxyz')
    0
    >>> similarity_Hunt_and_Szymanski('abcd','wxabyd')
    3
    """
    # let s1 be the shortest string
    if len(s1) > len(s2):
        s1, s2 = s2, s1
    equal = {}

    # particular cases
    if len(s1) == 0:
        return 0

    # first preprocessing step: computation of the equality points
    for i in range(0, len(s2)):
        equal[i + 1] = list_of_indices(s2[i], s1)[::-1]

    # second preprocessing step: similarity threshold table
    threshold = [len(s1) + 1 for _ in range(0, len(s2) + 1)]
    threshold[0] = 0
    # processing step: algorithm proper
    for i in range(0, len(s2)):
        for j in equal[i + 1]:
            k = look_for_threshold_index(j, threshold)  # look for k such that threshold[k-1] < j <= threshold[k]:
            if j < threshold[k]:
                threshold[k] = j

    # postprocessing step: looking for the result, i.e., the similarity between the two strings
    # it is the first index in threshold with a value different from len(s1) + 1, starting from the right
    result = 0
    for k in range(len(s2), 0, -1):
        if len(s1) + 1 != threshold[k]:
            result = k
            break
    return result

def list_of_indices(c, s):
    """
    Returns the list of indices of the occurrences of c in s
    """
    result = []
    i = 0
    while i < len(s):
        if type(s) == list:
            try:
                i = s[i:].index(c) + i + 1
            except ValueError:
                i = 0
        else:
            i = s.find(c, i) + 1

        if 0 != i:
            result.append(i - 1)
        else:
            break
    return result

def look_for_threshold_index(j, threshold, left=None, right=None):
    """
    Look for k such that threshold[k-1] < j <= threshold[k]
    Algorithm: dichotomy search

    >>> look_for_threshold_index(4,[4])
    0
    >>> look_for_threshold_index(4,[0, 1, 2, 3, 4, 5, 6, 7])
    4
    >>> look_for_threshold_index(5,[0, 2, 4, 6, 8, 10, 12, 14])
    3
    """

    if (None, None) == (left, right):
        left, right = 0, len(threshold) - 1

    if left > right:
        raise ValueError('Value in left higher than right')
    elif left + 1 == right or left == right:
        return right
    else:
        mid = int((left + right) / 2)
        if j <= threshold[mid]:
            left, right = left, mid
        else:
            left, right = mid, right
        return look_for_threshold_index(j, threshold, left, right)    

def NIL_Similarity(f1,f2):
    return get_similarity(f1, f2)