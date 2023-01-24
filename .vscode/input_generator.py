import random

numStates = 3
numTransitions = 3
numAcceptStates = 1
numInputStrings = 10

alphabet = ['a', 'b', 'eps']

transitions = []
for i in range(numTransitions):
    currentState = random.randint(0, numStates-1)
    inputSymbol = random.choice(alphabet)
    nextState = random.randint(0, numStates-1)
    transition = (currentState, inputSymbol, nextState)
    transitions.append(transition)

acceptStates = random.sample(range(numStates), numAcceptStates)

inputStrings = []
for i in range(numInputStrings):
    inputString = "".join([random.choice(alphabet) for _ in range(7)])
    inputStrings.append(inputString)

with open("test_case.txt", "w") as f:
    f.write(str(numStates) + "\n")
    f.write(str(len(transitions)) + "\n")

    for transition in transitions:
        f.write(str(transition[0]) + " " + transition[1] + " " + str(transition[2]) + "\n")
    
    f.write(str(numAcceptStates) + "\n")
    for acceptState in acceptStates:
        f.write(str(acceptState) + "\n")

    f.write(str(numInputStrings) + "\n")
    for inputString in inputStrings:
        if inputString == "eps":
            f.write("\n")
        else:
            f.write(inputString + "\n")
