import java.util.Random;
import java.util.Scanner;


public class C4 {
    final int N = 8;
    final int EMPTY = 0;
    final int CPU = 1;
    final int HUMAN = 2;
    Random rand = new Random();
    Scanner sc = new Scanner(System.in);
	int max_level = 4; 

    void resetBoard(int board[][]) {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    void printBoard(int board[][]) {
        System.out.println("\nCurrent Board:");
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                System.out.printf("%d ", board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    void dropToken(int board[][], int col, int player) {
        if(col < 0 || col >= N) return;
        
        for(int i = N-1; i >= 0; i--) {
            if(board[i][col] == EMPTY) {
                board[i][col] = player;
                return;
            }
        }
    }

    void HvsH() {
        int board[][] = new int[N][N];
        int col;
        int player = CPU;
        resetBoard(board);
        printBoard(board);

        while(true) {
            System.out.printf("Turn player %d: Input a column number (0-%d): ", player, N-1);
            col = sc.nextInt();
            if(col >= 0 && col < N && board[0][col] == EMPTY) {
                dropToken(board, col, player);
                printBoard(board);
                
                if(checkIfWins(board, player)) {
                    System.out.println("Player " + player + " wins!");
                    return;
                }
                
                player = (player == CPU) ? HUMAN : CPU;
            } else {
                System.out.println("Invalid column number. Try again.");
            }
        }
    }

    boolean horizontal(int board[][], int player) {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N-3; j++) {
                if(board[i][j] == player && 
                   board[i][j+1] == player && 
                   board[i][j+2] == player && 
                   board[i][j+3] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean vertical(int board[][], int player) {
        for(int i = 0; i < N-3; i++) {
            for(int j = 0; j < N; j++) {
                if(board[i][j] == player && 
                   board[i+1][j] == player && 
                   board[i+2][j] == player && 
                   board[i+3][j] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean diagonalRight(int board[][], int player) {
        for(int i = 0; i < N-3; i++) {
            for(int j = 0; j < N-3; j++) {
                if(board[i][j] == player && 
                   board[i+1][j+1] == player && 
                   board[i+2][j+2] == player && 
                   board[i+3][j+3] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean diagonalLeft(int board[][], int player) {
        for(int i = 0; i < N-3; i++) {
            for(int j = 3; j < N; j++) {
                if(board[i][j] == player && 
                   board[i+1][j-1] == player && 
                   board[i+2][j-2] == player && 
                   board[i+3][j-3] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean checkIfWins(int board[][], int player) {
        return horizontal(board, player) || 
               vertical(board, player) || 
               diagonalRight(board, player) || 
               diagonalLeft(board, player);
    }

    class Node {
        int board[][] = new int[N][N];
        Node children[] = new Node[N];
        int num_children;
        int score;
    }

    void copyBoard(int dest[][], int source[][]) {
        for(int i = 0; i < N; i++) {
            System.arraycopy(source[i], 0, dest[i], 0, N);
        }
    }

    int transformChild2Col(int board[][], int child_number) {
        int count = 0;
        for(int col = 0; col < N; col++) {
            if(board[0][col] == EMPTY) {
                if(count == child_number) {
                    return col;
                }
                count++;
            }
        }
        return -1;
    }

    int calculateNumOfChildren(int board[][]) {
        int count = 0;
        for(int i = 0; i < N; i++) {
            if(board[0][i] == EMPTY) {
                count++;
            }
        }
        return count;
    }

    Node createNode(int board[][], int child_number, int level) {
        Node p = new Node();
        copyBoard(p.board, board);
        int col = transformChild2Col(board, child_number);
        if(col == -1) return null;

        int player = (level % 2 == 1) ? CPU : HUMAN;
        dropToken(p.board, col, player);
        
        if(level == max_level){
            p.num_children = 0;
            p.score = scoring(p.board);
        } else {
            p.num_children = calculateNumOfChildren(p.board);
        }
        
        return p;
    }

    void createLevel(Node parent, int level) {
        for(int i = 0; i < parent.num_children; i++) {
            parent.children[i] = createNode(parent.board, i, level);
        }
    }

	void createTree(Node root, int level) {
        createLevel(root, level);
        for(int i = 0; i < root.num_children; i++) {
			createTree (root.children[i], level + 1);
        }
    }

    void printTree(Node node, int num_child, int level) {
        for(int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.printf("%d-%d\n", num_child, node.score);
        for(int i = 0; i < node.num_children; i++) {
            if(node.children[i] != null) {
                printTree(node.children[i], i, level+1);
            }
        }
    }

    void minimax(Node node, int level) {
        for(int i = 0; i < node.num_children; i++) {
            minimax(node.children[i], level+1);
        }
        
        if(node.num_children > 0) {
            if(level % 2 == 1) {
                int min = Integer.MAX_VALUE;
                for(int k = 0; k < node.num_children; k++) {
                    if(node.children[k].score < min) {
                        min = node.children[k].score;
                    }
                }
                node.score = min;
            } else {
                int max = Integer.MIN_VALUE;
                for(int k = 0; k < node.num_children; k++) {
                    if(node.children[k].score > max) {
                        max = node.children[k].score;
                    }
                }
                node.score = max;
            }
        }
    }

    int scoring(int board[][]) {
        if(checkIfWins(board, CPU)) return 1000;
        if(checkIfWins(board, HUMAN)) return -1000;
		return rand.nextInt(10); 
    }

    void dropComputerToken(Node root) {
        int bestChild = 0;
        for(int i = 1; i < root.num_children; i++) {
            if(root.children[i].score > root.children[bestChild].score) {
                bestChild = i;
            }
        }
        int col = transformChild2Col(root.board, bestChild);
        dropToken(root.board, col, CPU);
        System.out.println("Computer plays at column: " + col);
    }

    void playAgainstComputer() {
        int[][] board = new int[N][N];
        resetBoard(board);
        printBoard(board);

        while(true) {
            // 玩家回合
            System.out.printf("Your turn (Player %d): Input a column number (0-%d): ", HUMAN, N-1);
            int playerCol = sc.nextInt();
            if(playerCol >= 0 && playerCol < N && board[0][playerCol] == EMPTY) {
                dropToken(board, playerCol, HUMAN);
                printBoard(board);
                
                if(checkIfWins(board, HUMAN)) {
                    System.out.println("Congratulations! You win!");
                    return;
                }
                
                // 检查是否平局
                if(isBoardFull(board)) {
                    System.out.println("It's a draw!");
                    return;
                }
                
                // 电脑回合
                System.out.println("Computer is thinking...");
                Node root = new Node();
                copyBoard(root.board, board);
                root.num_children = calculateNumOfChildren(board);
                createTree(root, 1);
                minimax(root, 0);
                dropComputerToken(root);
                printBoard(root.board);
                
                if(checkIfWins(root.board, CPU)) {
                    System.out.println("Computer wins!");
                    return;
                }
                
                // 更新棋盘状态
                copyBoard(board, root.board);
                
                // 再次检查是否平局
                if(isBoardFull(board)) {
                    System.out.println("It's a draw!");
                    return;
                }
            } else {
                System.out.println("Invalid column number. Try again.");
            }
        }
    }

    boolean isBoardFull(int[][] board) {
        for(int j = 0; j < N; j++) {
            if(board[0][j] == EMPTY) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        C4 game = new C4();
        System.out.println("1. Human vs Human");
        System.out.println("2. Play against Computer");
        System.out.print("Choose game mode: ");
        
        int choice = game.sc.nextInt();
        
        if(choice == 1) {
            game.HvsH();
        } else {
            game.playAgainstComputer();  // 改为调用新的方法
        }
    }
}