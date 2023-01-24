import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NFA {

    private static char EPSILON = '\u03B5';

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

        boolean result = false;

        // Set the initial state
        int[] nextStates = new int[] { startState };

        if (input.length() == 0) {
            nextStates = applyTransition(nextStates, EPSILON);
            if(nextStates.length == 0){
                nextStates = addState(nextStates, 0);
            }
        } else {
            for (int i = 0; i < input.length(); i++) {
                nextStates = applyTransition(nextStates, input.charAt(i));
            }
        }

        for (int state : nextStates) {
            if (acceptStates.contains(state)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private int[] applyTransition(int[] currentStates, char input) {

        int nextState;
        int[] nextStates = new int[0];
        boolean[] visited = new boolean[numStates];
        for (int currentState : currentStates) {
            for (int j = 0; j < transitionTable.length; j++) {
                if (transitionTable[j][0] == currentState && input == transitionTable[j][1]) {
                    nextState = transitionTable[j][2];
                    nextStates = addState(nextStates, nextState);
                    nextStates = addEpsilonExpansion(nextState, nextStates, visited);
                }
            }
        }
        return nextStates;
    }

    private int[] addEpsilonExpansion(int state, int[] nextStates, boolean[] visited) {

        int nextEpsilonState;
        visited[state] = true;
        for (int i = 0; i < transitionTable.length; i++) {
            if (transitionTable[i][0] == state && transitionTable[i][1] == EPSILON) {
                nextEpsilonState = transitionTable[i][2];
                if (!visited[nextEpsilonState]) {
                    nextStates = addState(nextStates, nextEpsilonState);
                    nextStates = addEpsilonExpansion(nextEpsilonState, nextStates, visited);
                }
            }
        }
        return nextStates;
    }

    private int[] addState(int[] states, int newState) {
        int[] newStates = new int[states.length + 1];
        for (int i = 0; i < states.length; i++) {
            newStates[i] = states[i];
        }
        newStates[states.length] = newState;
        return newStates;
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
            System.out.println(accepted ? "accept" : "reject");
        }
    }

    private static String[] readInputStrings(Scanner scanner) {

        int numInputStrings = scanner.nextInt();
        String[] inputStrings = new String[numInputStrings];
        scanner.nextLine();

        for (int i = 0; i < numInputStrings; i++) {
            inputStrings[i] = scanner.nextLine();
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
            if (input.equalsIgnoreCase("eps")) {
                transitions[i][1] = EPSILON;
            } else {
                transitions[i][1] = input.charAt(0);
            }

            transitions[i][2] = scanner.nextInt();
        }

        return transitions;
    }
}
