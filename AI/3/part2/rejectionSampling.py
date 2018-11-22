# The rejection-sampling algorithm for answering queries given evidence in a Bayesian network.
import random
import xml.etree.ElementTree as ET

def formatInput(inputArgv):
    '''format the command line arguments which invoke the program, such as:
    python enumerationAlgo.py 1000 aima-alarm.xml B M true J true'''
    N = inputArgv[2]
    X = inputArgv[4]
    e = {}

    for i in range(0, len(inputArgv)):
        if inputArgv[i] == 'true':
        #if inputArgv[i].upper == 'TRUE':
            e.update({inputArgv[i-1]:True})
        elif inputArgv[i] == 'false':
            e.update({inputArgv[i - 1]: False})
    return N, X, e

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

                     #           bn.update({var: val})

# 'J': [['A'],  {(F,): .05, (T,): .90}]

    # vars = vars[::-1]   # inverted order, otherwise error - no parents in e
    # in prior sampling, we sample parents before children
    return bn, vars

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

def priorSample(bn):
    '''a sampling algorithm'''
    # vars = vars[::-1]   # first reverse variables so they are top down.

    vectorx = {}    # an event with n elements

    for var in vars:
        trueP = conditionalPro(var, True, vectorx, bn)
        ran = random.random()   # (0,1)
        if ran <= trueP:  # a random sample from P(Xi|parents(Xi))
            vectorx[var] = True
        else:
            vectorx[var] = False
        # vars = vars[::-1]   # reverse variable list again so it is the same as it was before this function was called..
    return vectorx  # an event sampled from the prior specified by bn


def consistent(vectorx, e):
    for key in vectorx:
        if key in e and vectorx[key] != e[key]:
            return False
    return True

def rejectionSampling(X, e, bn, N):
    '''inputs:
    X, the query variable
    e, observed values for variables E
    bn, a Bayesian network
    N, the total number of samples to be generated'''
    vectorN = {True: 0, False: 0}   # local variables, a vector of counts for each value of X, initially zero

    for j in range(1, int(N)+1):
        vectorx = priorSample(bn) # vector(x)

        if consistent(vectorx, e):
            x = vectorx[X]  # where x is the value of X in vector(x)
            vectorN[x] = vectorN[x] + 1 # count +1

    if vectorN[True] == 0 or vectorN[False] == 0:
        print 'The total number of samples to be generated is too small!'
        return None

    total = float(vectorN[True] + vectorN[False])
    QX = {True: vectorN[True] / total, False: vectorN[False] / total}

    return QX # an estimate of P(X|e)
    # return [QX, N]

if __name__ == '__main__':
    # get the problem into the program
    argv = 'python enumerationAlgo.py 100000 aima-alarm.xml B M true J true'
    # argv = 'python enumerationAlgo.py 1000 aima-wet-grass.xml R S true'
    # argv = raw_input('input the program invoking command, such as "python enumerationAlgo.py 1000 aima-alarm.xml B M true J true"')
    info = argv.split(' ')
    N, X, e = formatInput(info)
    print N, X, e
    bn, vars = parser(info[3])
    print bn, vars
    # X, e = formatInput(sys.argv)  # one-word character
    # bn, vars = parser(sys.argv[2])
    print rejectionSampling(X, e, bn, N)