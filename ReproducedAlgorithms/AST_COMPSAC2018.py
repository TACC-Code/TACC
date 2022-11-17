import numpy as np
seq1='CGUATTGA'
seq2='CGATGA'
import os
import sys
import javalang
from anytree import AnyNode
from javalang.ast import Node
import csv
import time
import pickle

nodetypedict = {'MethodDeclaration': 0, 'Modifier': 1, 'FormalParameter': 2, 'ReferenceType': 3, 'BasicType': 4,
     'LocalVariableDeclaration': 5, 'VariableDeclarator': 6, 'MemberReference': 7, 'ArraySelector': 8, 'Literal': 9,
     'BinaryOperation': 10, 'TernaryExpression': 11, 'IfStatement': 12, 'BlockStatement': 13, 'StatementExpression': 14,
     'Assignment': 15, 'MethodInvocation': 16, 'Cast': 17, 'ForStatement': 18, 'ForControl': 19,
     'VariableDeclaration': 20, 'TryStatement': 21, 'ClassCreator': 22, 'CatchClause': 23, 'CatchClauseParameter': 24,
     'ThrowStatement': 25, 'WhileStatement': 26, 'ArrayInitializer': 27, 'ReturnStatement': 28, 'Annotation': 29,
     'SwitchStatement': 30, 'SwitchStatementCase': 31, 'ArrayCreator': 32, 'This': 33, 'ConstructorDeclaration': 34,
     'TypeArgument': 35, 'EnhancedForControl': 36, 'SuperMethodInvocation': 37, 'SynchronizedStatement': 38,
     'DoStatement': 39, 'InnerClassCreator': 40, 'ExplicitConstructorInvocation': 41, 'BreakStatement': 42,
     'ClassReference': 43, 'SuperConstructorInvocation': 44, 'ElementValuePair': 45, 'AssertStatement': 46,
     'ElementArrayValue': 47, 'TypeParameter': 48, 'FieldDeclaration': 49, 'SuperMemberReference': 50,
     'ContinueStatement': 51, 'ClassDeclaration': 52, 'TryResource': 53, 'MethodReference': 54,
     'LambdaExpression': 55, 'InferredFormalParameter': 56}

nodetype6 = {'ClassDeclaration': 'c', 'FormalParameter': 'c', 'CatchClauseParameter': 'c', 'TypeParameter': 'c',
            'InferredFormalParameter': 'c', 'LocalVariableDeclaration': 'c', 'VariableDeclarator': 'c',
            'VariableDeclaration': 'c', 'MethodDeclaration': 'c', 'Modifier': 'c', 'ReferenceType': 'c',
            'MemberReference': 'c', 'ArraySelector': 'c', 'ClassCreator': 'c', 'ArrayInitializer': 'c',
            'Annotation': 'c', 'ArrayCreator': 'c', 'ConstructorDeclaration': 'c', 'FieldDeclaration': 'c',
            'TypeArgument': 'c', 'InnerClassCreator': 'c', 'ClassReference': 'c', 'ElementValuePair': 'c',
            'ElementArrayValue': 'c', 'SuperMemberReference': 'c', 'MethodReference': 'c',
            'SwitchStatement': 'b', 'SwitchStatementCase': 'b', 'DoStatement': 'b', 'SynchronizedStatement': 'b',
            'ReturnStatement': 'b', 'WhileStatement': 'b', 'ThrowStatement': 'b', 'TryStatement': 'b',
            'ForStatement': 'b', 'StatementExpression': 'b','BlockStatement': 'b', 'IfStatement': 'b',
            'BreakStatement': 'b', 'AssertStatement': 'b', 'ContinueStatement': 'b', 'ForControl': 'b',
            'MethodInvocation': 'b', 'Cast': 'b', 'CatchClause': 'b', 'EnhancedForControl': 'b', 'SuperMethodInvocation': 'b',
            'ExplicitConstructorInvocation': 'b', 'SuperConstructorInvocation': 'b', 'TryResource': 'b',
            'TernaryExpression': 'e', 'LambdaExpression': 'e', 'BinaryOperation': 'e',
            'Operator': 'o', 'Assignment': 'o',
            'Keyword': 'k', 'Boolean': 'k', 'This': 'k', 'BasicType': 'k', 'Null': 'k', 'Separator': 'k',
            'Literal': 'l', 'OctalInteger': 'l', 'DecimalInteger': 'l', 'HexInteger': 'l', 'DecimalFloatingPoint': 'l',
            'Identifier': 'l', 'String': 'l'}


def get_token(node):
    token = ''
    # print(isinstance(node, Node))
    # print(type(node))
    if isinstance(node, str):
        token = node
    elif isinstance(node, set):
        token = 'Modifier'
    elif isinstance(node, Node):
        token = node.__class__.__name__
    # print(node.__class__.__name__,str(node))
    # print(node.__class__.__name__, node)
    return token


def get_child(root):
    # print(root)
    if isinstance(root, Node):
        children = root.children
    elif isinstance(root, set):
        children = list(root)
    else:
        children = []

    def expand(nested_list):
        for item in nested_list:
            if isinstance(item, list):
                for sub_item in expand(item):
                    # print(sub_item)
                    yield sub_item
            elif item:
                # print(item)
                yield item

    return list(expand(children))


def createtree(root, node, nodelist, parent=None):
    id = len(nodelist)
    # print(id)
    token, children = get_token(node), get_child(node)
    if id == 0:
        root.token = token
        root.data = node
    else:
        newnode = AnyNode(id=id, token=token, data=node, parent=parent)
    nodelist.append(token)
    for child in children:
        if id == 0:
            createtree(root, child, nodelist, parent=root)
        else:
            createtree(root, child, nodelist, parent=newnode)

def Smith_Waterman(seq1, seq2, mS, mmS, w1):
    maxScore = 0
    S = np.zeros([len(seq1) + 1, len(seq2) + 1], int)

    for i in range(1, len(seq1) + 1):
        for j in range(1, len(seq2) + 1):
            if seq1[i - 1] == seq2[j - 1]:
                L = S[i - 1, j - 1] + mS
            else:
                L = S[i - 1, j - 1] + mmS
            P = S[i - 1, j] - w1
            Q = S[i, j - 1] - w1
            S[i, j] = max(L, P, Q, 0)
            maxScore = max(S[i, j], maxScore)

    return maxScore/mS

# def Smith_Waterman(seq1, seq2, mS, mmS, w1):

#     S = np.zeros([len(seq1) + 1, len(seq2) + 1], int)

#     for i in range(0, len(seq1) + 1):
#         for j in range(0, len(seq2) + 1):
#             if i == 0 or j == 0:
#                 continue
#             else:
#                 if seq1[i - 1] == seq2[j - 1]:
#                     s = mS
#                 else:
#                     s = mmS
#                 L = S[i - 1, j - 1] + s
#                 P = S[i - 1, j] - w1
#                 Q = S[i, j - 1] - w1
#                 S[i, j] = max(L, P, Q, 0)

#     # print("S = ", S)
#     score = S[len(seq1), len(seq2)]
#     # print(score)
#     return score


def one_feature(path):

    # 生成ast和token
    programfile = open(path, encoding='utf-8')
    programtext = programfile.read()
    programtokens = javalang.tokenizer.tokenize(programtext)
    parser = javalang.parse.Parser(programtokens)
    tree = parser.parse_member_declaration()

    programfile.close()

    file = open(path, "r", encoding='utf-8')
    tokens = list(javalang.tokenizer.tokenize(file.read()))
    # print("programtokens", list(tokens))
    file.close()

    # 生成树
    nodelist = []
    newtree = AnyNode(id=0, token=None, data=None)
    createtree(newtree, tree, nodelist)

    # 生成类型字典
    typedict = {}
    for token in tokens:
        token_type = str(type(token))[:-2].split(".")[-1]
        token_value = token.value
        if token_value not in typedict:
            typedict[token_value] = token_type
        else:
            if typedict[token_value] != token_type:
                print('!!!!!!!!')
    seq = ''
    for i in range(len(nodelist)):
        node = nodelist[i]
        if node not in nodetypedict:
            try:
                node = typedict[node]
            except KeyError:
                node = 'String'
        seq += nodetype6[node]
    # print(seq)
    return seq


def compare(file1, file2):
    start = time.time()
    seq1 = one_feature(file1)
    seq2 = one_feature(file2)
    # file = open('size/a3.txt','ab')
    # pickle.dump((seq1,seq2),file)
    # file.close()
    # size_a = sys.getsizeof(seq1)
    # size_b = sys.getsizeof(seq2)
    # size = size_a + size_b
    end = time.time()
    score1 = Smith_Waterman(seq1, seq1, 2, -2, 1)
    score2 = Smith_Waterman(seq2, seq2, 2, -2, 1)
    score = Smith_Waterman(seq1, seq2, 2, -2, 1)
    sim = 2*score/(score1+score2)
    return sim #,size


def main(pairspath):
    pairs = csv.reader(open(pairspath, 'r', encoding='gbk'))
    path = 'C://Users//84324//Desktop//pairs//'

    for pair in pairs:
        pair1 = path + pair[0] + '.java'
        pair2 = path + pair[1] + '.java'
        sim = compare(pair1, pair2)
        print(pair1)
        print(pair2)
        print(sim)


def main1():
    pair1 = 'test.java'
    pair2 = 'test2.java'
    sim = compare(pair1, pair2)
    print(pair1)
    print(pair2)
    print(sim)

def COMPSAC2018(f1,f2):
    sim = compare(f1, f2)
    #print(sim)
    return sim
