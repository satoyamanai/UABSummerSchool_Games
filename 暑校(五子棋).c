#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <limits.h>

#define N 8
#define EMPTY 0
#define PLAYER 1   // Human player
#define COMPUTER 2 // Computer
#define MAX_DEPTH 4 // Even number as required (2,4,6)

typedef struct Node {
    int board[N][N];
    struct Node* children[N];
    int num_children;
    int score;
    int move_col; // The column that led to this board state
} Node;


// Copy board state  复制出来的模拟磁盘，用于计算机决策思考不同的结果
void copyBoard(int dest[N][N], int src[N][N]) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            dest[i][j] = src[i][j];
        }
    }
}

// Print the board
void printBoard(int board[N][N]) {
    printf("\n");
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            printf("| %d ", board[i][j]);
        }
        printf("|\n");
    }
    for (int j = 0; j < N; j++) {
        printf("  %d ", j+1);
    }
    printf("\n\n");
}

// Drop a token in the specified column
int dropToken(int board[N][N], int col, int player) {
    for (int i = N-1; i >= 0; i--) {
        if (board[i][col] == EMPTY) {
            board[i][col] = player;
            return 1; // Success
        }
    }
    return 0; // Column full
}

// Calculate number of possible moves (non-full columns)
int calculateNumOfChildren(int board[N][N]) {
    int count = 0;
    for (int j = 0; j < N; j++) {
        if (board[0][j] == EMPTY) {
            count++;
        }
    }
    return count;
}

// Transform child number to actual column number, skipping full columns
int transformChild2Col(int board[N][N], int child_number) {
    int col = 0;
    int count = 0;
    
    for (col = 0; col < N; col++) {
        if (board[0][col] == EMPTY) {
            if (count == child_number) {
                return col;
            }
            count++;
        }
    }
    return -1; // Shouldn't happen if called correctly
}

// Create a new node with the board state after a move
Node* createNode(int board[N][N], int child_number, int current_player) {
    Node *p = malloc(sizeof(Node));
    copyBoard(p->board, board);
    int col = transformChild2Col(p->board, child_number);
    dropToken(p->board, col, current_player);
    p->num_children = calculateNumOfChildren(p->board);
    p->move_col = col;
    p->score = 0;
    
    for (int i = 0; i < N; i++) {
        p->children[i] = NULL;
    }
    
    return p;
}

// Create all children for a node
void createLevel(Node *parent, int current_player, int depth) {
    if (depth >= MAX_DEPTH) return;
    
    for (int i = 0; i < parent->num_children; i++) {
        parent->children[i] = createNode(parent->board, i, current_player);
    }
}

// Recursively create the game tree
void createTree(Node *root, int depth) {
    if (depth >= MAX_DEPTH) return;
    
    int next_player = (depth % 2 == 0) ? COMPUTER : PLAYER;
    createLevel(root, next_player, depth);
    
    for (int i = 0; i < root->num_children; i++) {
        createTree(root->children[i], depth+1);
    }
}

// Evaluate a board state (heuristic function)
int evaluateBoard(int board[N][N]) {
    // Check if computer wins
    if (checkWin(board, COMPUTER)) {
        return 1000;
    }
    // Check if human wins
    if (checkWin(board, PLAYER)) {
        return -1000;
    }
    
    // Simple heuristic: count potential lines
    int score = 0;
    
    // Add more sophisticated evaluation here if needed
    // For example: count number of 3-in-a-row, 2-in-a-row, etc.
    
    return score;
}

// MINIMAX algorithm implementation
int minimax(Node *node, int depth, int isMaximizing) {
    // Leaf node - evaluate the board
    if (depth == MAX_DEPTH || node->num_children == 0) {
        node->score = evaluateBoard(node->board);
        return node->score;
    }
    
    if (isMaximizing) {
        int bestValue = INT_MIN;
        for (int i = 0; i < node->num_children; i++) {
            int value = minimax(node->children[i], depth+1, 0);
            bestValue = (value > bestValue) ? value : bestValue;
        }
        node->score = bestValue;
        return bestValue;
    } else {
        int bestValue = INT_MAX;
        for (int i = 0; i < node->num_children; i++) {
            int value = minimax(node->children[i], depth+1, 1);
            bestValue = (value < bestValue) ? value : bestValue;
        }
        node->score = bestValue;
        return bestValue;
    }
}

// Check if a player has won
int checkWin(int board[N][N], int player) {
    // Check horizontal
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N-3; j++) {
            if (board[i][j] == player && board[i][j+1] == player && 
                board[i][j+2] == player && board[i][j+3] == player) {
                return 1;
            }
        }
    }
    
    // Check vertical
    for (int j = 0; j < N; j++) {
        for (int i = 0; i < N-3; i++) {
            if (board[i][j] == player && board[i+1][j] == player && 
                board[i+2][j] == player && board[i+3][j] == player) {
                return 1;
            }
        }
    }
    
    // Check diagonal (top-left to bottom-right)
    for (int i = 0; i < N-3; i++) {
        for (int j = 0; j < N-3; j++) {
            if (board[i][j] == player && board[i+1][j+1] == player && 
                board[i+2][j+2] == player && board[i+3][j+3] == player) {
                return 1;
            }
        }
    }
    
    // Check diagonal (top-right to bottom-left)
    for (int i = 0; i < N-3; i++) {
        for (int j = 3; j < N; j++) {
            if (board[i][j] == player && board[i+1][j-1] == player && 
                board[i+2][j-2] == player && board[i+3][j-3] == player) {
                return 1;
            }
        }
    }
    
    return 0;
}

// Check if board is full
int isBoardFull(int board[N][N]) {
    for (int j = 0; j < N; j++) {
        if (board[0][j] == EMPTY) {
            return 0;
        }
    }
    return 1;
}

// Computer makes a move using MINIMAX
int computerMove(int board[N][N]) {
    // Create root node
    Node *root = malloc(sizeof(Node));
    copyBoard(root->board, board);
    root->num_children = calculateNumOfChildren(root->board);
    root->score = 0;
    root->move_col = -1;
    
    // Create the game tree
    createTree(root, 0);
    
    // Run MINIMAX algorithm
    minimax(root, 0, 1);
    
    // Find the best move (child with highest score)
    int bestScore = INT_MIN;
    int bestMove = -1;
    
    for (int i = 0; i < root->num_children; i++) {
        if (root->children[i]->score > bestScore) {
            bestScore = root->children[i]->score;
            bestMove = root->children[i]->move_col;
        }
    }
    
    // Free the tree
    freeTree(root);
    
    return bestMove;
}

// Free the memory used by the tree
void freeTree(Node *root) {
    if (root == NULL) return;
    
    for (int i = 0; i < root->num_children; i++) {
        freeTree(root->children[i]);
    }
    
    free(root);
}

int main() {
    int board[N][N] = {EMPTY}; // Initialize empty board
    int game_over = 0;
    int col;
    
    printf("Connect-4 Game: Human vs Computer\n");
    printf("Human player is 1, Computer is 2\n");
    
    while (!game_over) {
        printBoard(board);
        
        // Human turn
        printf("Your turn (enter column 1-8): ");
        scanf("%d", &col);
        col--; // Convert to 0-based index
        
        while (col < 0 || col >= N || board[0][col] != EMPTY) {
            printf("Invalid move. Try again (1-8): ");
            scanf("%d", &col);
            col--;
        }
        
        dropToken(board, col, PLAYER);
        printBoard(board);
        
        if (checkWin(board, PLAYER)) {
            printf("Congratulations! You won!\n");
            game_over = 1;
            break;
        }
        
        if (isBoardFull(board)) {
            printf("It's a draw!\n");
            game_over = 1;
            break;
        }
        
        // Computer turn
        printf("Computer is thinking...\n");
        col = computerMove(board);
        dropToken(board, col, COMPUTER);
        
        if (checkWin(board, COMPUTER)) {
            printBoard(board);
            printf("Computer wins!\n");
            game_over = 1;
            break;
        }
        
        if (isBoardFull(board)) {
            printBoard(board);
            printf("It's a draw!\n");
            game_over = 1;
            break;
        }
    }
    
    return 0;
}