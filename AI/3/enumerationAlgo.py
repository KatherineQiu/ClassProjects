# The enumeration algorithm for answering queries on Bayesian networks, without speedups.
import sys
import argparse
import xml.etree.ElementTree as ET
import matplotlib.pyplot as plt
import time


def formatInput(inputArgv):
    '''format the command line arguments which invoke the program, such as:
    python enumerationAlgo.py aima-alarm.xml B M true J true'''
    X = inputArgv[1]
    e = {}

    for i in range(0, len(inputArgv)):
        if inputArgv[i] == 'true':
            # if inputArgv[i].upper == 'TRUE':
            e.update({inputArgv[i - 1]: True})
        elif inputArgv[i] == 'false':
            e.update({inputArgv[i - 1]: False})
    return X, e


def parser(file_name):
    '''parse .xml file - extract variables, CPT...'''
    tree = ET.parse(file_name)
    root = tree.getroot()

    bn = {}
    vars = []

    if root.tag != 'BIF':
        print 'Only the semi-standard XMLBIF representation can be processed.'
    else:
        root = root[0]
        if root.tag != 'NETWORK':
            print 'The file misses a NETWORK tag.'
        else:
            for i in range(1, len(root)):  # skip the network name

                if root[i].tag == 'VARIABLE':
                    # assumption: all variables have Boolean value
                    vars.append(root[i][0].text)

                elif root[i].tag == 'DEFINITION':

                    for j in range(0, len(root[i])):
                        parents = []
                        values = {}
                        if root[i][j].tag == 'FOR':
                            var = root[i][j].text

                            val = [parents, values]
                            if len(root[i]) == 2:  # no parents
                                if root[i][1].tag == 'TABLE':
                                    # print root[i][1].text
                                    prob = float(root[i][1].text.split(' ')[0])
                                    bn.update({var: [[], {None: prob}]})

                            elif len(root[i]) != 2:
                                for g in range(1, len(root[i])):
                                    if root[i][g].tag == 'GIVEN':
                                        parents.append(root[i][g].text)
                                        bn.update({var: val})

                                    elif root[i][g].tag == 'TABLE':
                                        prob = [x.strip() for x in root[i][g].text.strip().split('\n')]
                                        for r in range(0, len(prob)):
                                            p = float(prob[r].strip().split(' ')[0])
                                            if len(prob) == 2:
                                                if r == 0:
                                                    values.update({(True,): p})
                                                if r == 1:
                                                    values.update({(False,): p})

                                            if len(prob) == 4:
                                                if r == 0:
                                                    values.update({(True, True): p})
                                                if r == 1:
                                                    values.update({(True, False): p})
                                                if r == 2:
                                                    values.update({(False, True): p})
                                                if r == 3:
                                                    values.update({(False, False): p})

    # 'J': [['A'],  {(F,): .05, (T,): .90}]

    vars = vars[::-1]  # inverted order, otherwise error - no parents in e
    return bn, vars


def normalize(QX):
    '''normalize QX so that the probabilities add up to 1'''
    sum = 0.0
    for value in QX.values():
        sum += value
    for key in QX.keys():
        QX[key] /= sum
    return QX


def conditionalPro(var, val, e, bn):
    '''get conditional probability'''
    parents = bn[var][0]
    if len(parents) == 0:
        trueP = bn[var][1][None]
    else:
        parentVals = [e[parent] for parent in parents]
        trueP = bn[var][1][tuple(parentVals)]

    if val == True:
        return trueP
    else:
        return 1.0 - trueP


def enumerationAsk(X, e, bn, vars):
    '''inputs:
    X: the query variable
    e: observed values for variables E
    bn: a Bayes net with variables'''
    QX = {}  # a distribution over X, initially empty
    for xi in [False, True]:
        e[X] = xi  # e extended with X=xi
        QX[xi] = enumerateAll(vars, e, bn)
        del e[X]
    return normalize(QX)  # a distribution over X


def enumerateAll(vars, e, bn):
    if len(vars) == 0:
        return 1.0

    Y = vars.pop()
    if Y in e:
        P = conditionalPro(Y, e[Y], e, bn)
        val = P * enumerateAll(vars, e, bn)
        vars.append(Y)
        return val  # a real number
    else:  # sum of T&F
        sum = 0
        e[Y] = True
        sum += conditionalPro(Y, True, e, bn) * enumerateAll(vars, e, bn)
        e[Y] = False
        sum += conditionalPro(Y, False, e, bn) * enumerateAll(vars, e, bn)
        del e[Y]
        vars.append(Y)
        return sum  # a real number


if __name__ == '__main__':
    # get the problem into the program
    # argv = 'python enumerationAlgo.py aima-alarm.xml B M true J true'
    # argv = 'python enumerationAlgo.py aima-wet-grass.xml R S true'
    # argv = raw_input('input the program invoking command, such as "python enumerationAlgo.py aima-alarm.xml B M true J true"')
    time1=time.time()
    pars = argparse.ArgumentParser()
    pars.add_argument('paras', type=str,nargs='*')
    args = pars.parse_args()
    print(args.paras)

    X, e = formatInput(args.paras)
    print X, e
    bn, vars = parser(args.paras[0])
    print bn, vars
    # X, e = formatInput(sys.argv)  # one-word character
    # bn, vars = parser(sys.argv[2])
    print enumerationAsk(X, e, bn, vars)
    print(time.time()-time1)
