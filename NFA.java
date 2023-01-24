import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NFA {
    private int numStates;
    private int startState;
    private Set<Integer> acceptStates;
    private int[][] transitionTable;

    public NFA(int numStates, int startState, Set<Integer> acceptStates, int[][] transitionTable) {
        this.numStates = numStates;
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.transitionTable = transitionTable;
    }

    public boolean simulate(String input) {
        Set<Integer> currentStates = new HashSet<>();
        currentStates.add(startState);
        currentStates = epsilonClosure(currentStates);

        for (int i = 0; i < input.length(); i++) {
            Set<Integer> nextStates = new HashSet<>();
            for (int state : currentStates) {
                int inputChar = input.charAt(i);
                nextStates.addAll(transition(state, inputChar));
            }
            currentStates = epsilonClosure(nextStates);
        }

        for (int state : currentStates) {
            if (acceptStates.contains(state)) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> epsilonClosure(Set<Integer> states) {
        Set<Integer> closure = new HashSet<>(states);
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int state : closure) {
                for (int nextState : transition(state, 0)) {
                    if (closure.add(nextState)) {
                        changed = true;
                    }
                }
            }
        }
        return closure;
    }

    private Set<Integer> transition(int state, int input) {
        Set<Integer> nextStates = new HashSet<>();
        for (int i = 0; i < numStates; i++) {
            if (transitionTable[state][i] == input) {
                nextStates.add(i);
            }
        }
        return nextStates;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int numStates = sc.nextInt();
        int[][] transitions = readTransitions(sc);
        Set<Integer> acceptStates = readAcceptStates(sc);
        String[] inputStrings = readInputStrings(sc);

        int startState = 0;
        NFA nfa = new NFA(numStates, startState, acceptStates, transitions);

        for (String inputString : inputStrings) {
            boolean accepted = nfa.simulate(inputString);
            System.out.println(accepted ? "Accepted" : "Rejected");
        }
    }

    private static String[] readInputStrings(Scanner scanner) {

        int numInputStrings = scanner.nextInt();
        String[] inputStrings = new String[numInputStrings];

        for (int i = 0; i < numInputStrings; i++) {
            inputStrings[i] = scanner.next();
        }

        return inputStrings;
    }

    private static Set<Integer> readAcceptStates(Scanner scanner) {

        int numAcceptStates = scanner.nextInt();
        Set<Integer> acceptStates = new HashSet<>();

        for (int i = 0; i < numAcceptStates; i++) {
            acceptStates.add(scanner.nextInt());
        }

        return acceptStates;
    }

    private static int[][] readTransitions(Scanner scanner) {

        String input = null;
        int numTransitions = scanner.nextInt();
        int[][] transitions = new int[numTransitions][3];

        for (int i = 0; i < numTransitions; i++) {

            transitions[i][0] = scanner.nextInt();
            input = scanner.next();
            if(input.equalsIgnoreCase("eps")){
                transitions[i][1] = '\u03B5';
            }else{
                transitions[i][1] = input.charAt(0);
            }

            transitions[i][2] = scanner.nextInt();
        }

        return transitions;
    }
}
