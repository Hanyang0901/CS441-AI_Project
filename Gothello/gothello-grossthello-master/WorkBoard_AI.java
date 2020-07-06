import java.util.Vector;

public class WorkBoard_AI extends Board {
    static final int INF = 5 * 5 + 1;
    Move best_move = null;

    public WorkBoard_AI() {
    }

    public WorkBoard_AI(WorkBoard_AI w) {
		super(w);
    }

	// chech whether (x,y) is the "eyes", return its owner PLAYER_WHITE or PLAYER_BLACK
	int isEye(int x, int y){
		int []d = {-1,0, 0,1, 1,0, 0,-1};
		int eye_owner = 0;
		for(int i=0; i<8; i+=2){
			int xx = x+d[i];
			int yy = y+d[i+1];
			if( 0<=xx && xx<5 && 0<=yy && yy<5 ){
				if( square[xx][yy]==0 ) return 0;
				if( eye_owner==0 ) eye_owner = square[xx][yy];
				else if( square[xx][yy]!=eye_owner ) return 0;
			}
		}
		return eye_owner;
	}

    int heval() { // evaluate the score of current board
		int score = 0;
		for (int i = 0; i < 5; i++)
	    	for (int j = 0; j < 5; j++)
				if (square[i][j] == checker_of(to_move)) score += 1;
	        	else if (square[i][j] == checker_of(opponent(to_move))) score -= 1;
				else{
					int eye_owner = isEye(i,j);
					if( eye_owner == to_move ) score += 3;
					else if( eye_owner == opponent(to_move) ) score -= 3;
				}
		return score;
    }

    static java.util.Random prng = new java.util.Random();

    static int randint(int n) {
		return Math.abs(prng.nextInt()) % n;
    }
	
	int minimax(int depth, int alpha, int beta, int maxDepth){
		int flag = (depth%2==0 ? 1:-1);
		
		Vector<Move> moves = genMoves();
		int nmoves = moves.size();
		if (nmoves == 0) { // game over
		    best_move = new Move();
		    WorkBoard_AI scratch = new WorkBoard_AI(this);
		    int status = scratch.try_move(best_move);
		    if (status != GAME_OVER){ // game end for current player
				//System.out.println("error: nmoves==0");
				return scratch.minimax(depth+1, alpha, beta, maxDepth);
			}
		    int result = scratch.referee();
		    if (result == to_move) return 10000*flag;  // current player win
		    if (result == opponent(to_move)) return -10000*flag; // opponent win
		    return 0;
		}
		
		if (depth >= maxDepth) { // reach to the max search depth, return the score of current board
		    return heval()*flag;
		}
		
		int maxScore = -10001, minScore = 10001;
		int last_rand = randint(100);
		for (int i = 0; i < nmoves; i++) { // go through all the available moves
		    Move m = moves.get(i);
		    WorkBoard_AI scratch = new WorkBoard_AI(this);
		    int status = scratch.try_move(m);
		    if (status == ILLEGAL_MOVE)
				throw new Error("unexpectedly illegal move");
		    if (status == GAME_OVER)
				throw new Error("unexpectedly game over");
			int score = scratch.minimax(depth+1, alpha, beta, maxDepth);
			//if( depth==0 ) System.out.println(depth+": "+m.x+","+m.y+" = "+score);
			
			if( depth%2==0 ){ // 0/2/4/... represent player's turn. get the max score
				if( maxScore==score ){
					int new_rand = randint(2);
					if( last_rand<new_rand ){
						last_rand = new_rand;
						best_move = moves.get(i);
					}
				}
				if( maxScore < score ){ // get the max score
					maxScore = score;
					if( depth==0 ) best_move = moves.get(i);
				}
				if( score>alpha ) alpha = score;
			}else{ // 1/3/5/... represent the opponent's turn. get the min score
				if( minScore>score ) minScore = score; // get the min score
				if( score<beta ) beta = score;
			}
			if( alpha >= beta ) break; // alpha-beta pruning algorithm, to speed up the search
		}
		return (depth%2==0 ? maxScore:minScore);
	}
	
    void bestMove(int depth) {
		// alpha, beta :  used for alpha-beta pruning algorithm
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int v = minimax(0, alpha, beta, depth);
    }
}
