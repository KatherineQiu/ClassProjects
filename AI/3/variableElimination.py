# The enumeration algorithm for answering queries on Bayesian networks, without speedups.
import sys
import xml.etree.ElementTree as ET

def formatInput(inputArgv):
    '''format the command line arguments which invoke the program, such as:
    python enumerationAlgo.py aima-alarm.xml B M true J true'''
    X = inputArgv[3]
    e = {}

    for i in range(0, len(inputArgv)):
        if inputArgv[i] == 'true':
        #if inputArgv[i].upper == 'TRUE':
            e.update({inputArgv[i-1]:True})
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
            for i in range(1, len(root)):   # skip the network name


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
                                        prob = root[i][g].text.strip().split('\n\t')
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
    if len(parents) == 0:   # no parents
        trueP = bn[var][1][None]
    else:
        parentVals = [e[parent] for parent in parents]
        trueP = bn[var][1][tuple(parentVals)]

    if val == True:
        return trueP
    else:
        return 1.0 - trueP


def eliminationAsk(X, e, bn, vars):
    '''inputs:
    X: the query variable
    e: observed values for variables E
    bn: a Bayesian network specifying joint distribution P(X1, ..., Xn)'''



if __name__ == '__main__':
    # get the problem into the program
    # argv = 'python enumerationAlgo.py aima-alarm.xml B M true J true'
    argv = 'python enumerationAlgo.py aima-wet-grass.xml R S true'
    # argv = raw_input('input the program invoking command, such as "python enumerationAlgo.py aima-alarm.xml B M true J true"')
    info = argv.split(' ')
    X, e = formatInput(info)
    print X, e
    bn, vars = parser(info[2])
    print bn, vars
    # X, e = formatInput(sys.argv)  # one-word character
    # bn, vars = parser(sys.argv[2])
    print eliminationAsk(X, e, bn, vars)
