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
        Set<Integer> nextStates = new HashSet<>();
        nextStates.add(0);

        for (int i = 0; i < input.length(); i++) {
            nextStates = applyTransition(nextStates, input.charAt(i));
        }

        for(int state : nextStates){
            if(acceptStates.contains(state)){
                result = true;
                break;
            }
        }
        return result;
    }

    private Set<Integer> applyTransition(Set<Integer> currentStates, char input) {

        int nextState;
        Set<Integer> nextStates = new HashSet<>();
        for (int currentState : currentStates) {
            for (int j = 0; j < transitionTable.length; j++) {
                if (transitionTable[j][0] == currentState && input == transitionTable[j][1]) {
                    nextState = transitionTable[j][2];
                    nextStates.add(nextState);
                    nextStates.addAll(epsilonExpansion(nextState));
                }
            }
        }
        return nextStates;
    }

    private Set<Integer> epsilonExpansion(int state) {
        int nextEpsilonState;
        Set<Integer> epsExpansion = new HashSet<>();
        for (int i = 0; i < transitionTable.length; i++) {
            if (transitionTable[i][0] == state && transitionTable[i][1] == EPSILON) {
                nextEpsilonState = transitionTable[i][2];
                if (!epsExpansion.contains(nextEpsilonState)) {
                    epsExpansion.add(nextEpsilonState);
                    epsilonExpansion(nextEpsilonState);
                }
            }
        }
        return epsExpansion;
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
