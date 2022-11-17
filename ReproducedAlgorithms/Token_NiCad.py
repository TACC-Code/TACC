import javalang
import os
import sys
import pickle
def getCodeBlock(file_path):     #只对变量名做了归一化
    block = []
    # print(file_path)
    with open(file_path, 'r') as temp_file:
        lines = temp_file.readlines()
        for line in lines:
            tokens = list(javalang.tokenizer.tokenize(line))
            for token in tokens:
                if (type(token) == javalang.tokenizer.Identifier):
                    block.append("id")
                else:
                    block.append(token.value)
    return block


def get_similarity(block1_path, block2_path):
    # file = open('size/t1.txt','ab')
    block1 = getCodeBlock(block1_path)
    block2 = getCodeBlock(block2_path)
    # pickle.dump((block1,block2),file)
    # file.close()
    # size_a = sys.getsizeof(block1)
    # size_b = sys.getsizeof(block2)
    m, n = len(block1), len(block2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if block1[i - 1] == block2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + 1
            else:
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])

    return dp[m][n]/max(m, n)#, (size_a + size_b)

def NiCad_Similarity(f1,f2):
    return get_similarity(f1, f2)
