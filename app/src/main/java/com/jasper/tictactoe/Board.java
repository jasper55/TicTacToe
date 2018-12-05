package com.jasper.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Board extends AppCompatActivity {


    private static final int PLAYBOARD_DIM = 3;
    private TableLayout playboard;
    private TextView tvWhosTurn;
    private char [][] boardState;
    private char whosTurn;

    private static final int GAME_CONTINUE = 0;
    private static final int GAME_DRAW = -1;
    private static final int X_WINS = 1;
    private static final int O_WINS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        boardState = new char [PLAYBOARD_DIM][PLAYBOARD_DIM];
        playboard = (TableLayout) findViewById(R.id.mainBoard);
        tvWhosTurn = (TextView) findViewById(R.id.whos_turn);

        resetBoard();
        tvWhosTurn.setText("Turn: " + whosTurn);

        initBoard();

        Button resetBtn = (Button) findViewById(R.id.reset_Btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent current = getIntent();
                finish();
                startActivity(current);
            }
        });
    }

    private void initBoard() {
        for(int columnIndex = 0; columnIndex < playboard.getChildCount(); columnIndex++){
            TableRow row = (TableRow) playboard.getChildAt(columnIndex);
            for(int rowIndex = 0; rowIndex < row.getChildCount(); rowIndex++){
                TextView tvCell = (TextView) row.getChildAt(rowIndex);
                tvCell.setText(R.string.none);
                tvCell.setOnClickListener(Move(columnIndex, rowIndex, tvCell));
            }
        }
    }

    protected void resetBoard(){
        whosTurn = 'X';
        for(int i = 0; i< PLAYBOARD_DIM; i++){
            for(int j = 0; j< PLAYBOARD_DIM; j++){
                boardState[i][j] = ' ';
            }
        }
    }

    protected int updateGameStatus(){

        for(int cellIndex = 0; cellIndex < PLAYBOARD_DIM; cellIndex++){
            if(checkRowEquality(cellIndex,'X'))
                return X_WINS;
            if(checkColumnEquality(cellIndex, 'X'))
                return X_WINS;
            if(checkRowEquality(cellIndex,'O'))
                return O_WINS;
            if(checkColumnEquality(cellIndex,'O'))
                return O_WINS;
            if(checkDiagonal('X'))
                return X_WINS;
            if(checkDiagonal('O'))
                return O_WINS;
        }

        boolean boardFull = true;
        for(int i = 0; i< PLAYBOARD_DIM; i++){
            for(int j = 0; j< PLAYBOARD_DIM; j++){
                if(boardState[i][j]==' ')
                    boardFull = false;
            }
        }
        if(boardFull)
            return GAME_DRAW;
        else return GAME_CONTINUE;
    }

    protected boolean checkDiagonal(char playerChar){
        int playerCharCount = 0,computerCharCount = 0;

        for (int index = 0; index< PLAYBOARD_DIM; index++) {
            if (boardState[index][index] == playerChar)
                playerCharCount++;
        }

        for (int index = 0; index < PLAYBOARD_DIM; index++) {
            if (boardState[index][PLAYBOARD_DIM - 1 - index] == playerChar)
                computerCharCount++;
        }

        return (playerCharCount == PLAYBOARD_DIM || computerCharCount == PLAYBOARD_DIM);
    }

    protected boolean checkRowEquality(int rowIndex, char playerChar){
        int playerCharCount=0;

        for(int columnIndex = 0; columnIndex< PLAYBOARD_DIM; columnIndex++){
            if(boardState[rowIndex][columnIndex]==playerChar)
                playerCharCount++;
        }

        return (playerCharCount == PLAYBOARD_DIM);
    }

    protected boolean checkColumnEquality(int columnIndex, char playerChar){
        int playerCharCount=0;

        for(int rowIndex = 0; rowIndex< PLAYBOARD_DIM; rowIndex++){
            if(boardState[rowIndex][columnIndex]==playerChar)
                playerCharCount++;
        }

        return (playerCharCount == PLAYBOARD_DIM);
    }

    protected boolean isCellSet(int rowIndex, int columnIndex){
        return !(boardState[rowIndex][columnIndex]==' ');
    }

    protected void stopMatch(){
        for(int cellNumber = 0; cellNumber < playboard.getChildCount(); cellNumber++){
            TableRow rowIndex = (TableRow) playboard.getChildAt(cellNumber);
            for(int columnIndex = 0; columnIndex < rowIndex.getChildCount(); columnIndex++){
                TextView tv = (TextView) rowIndex.getChildAt(columnIndex);
                tv.setOnClickListener(null);
            }
        }
    }

    View.OnClickListener Move(final int rowIndex, final int columnIndex, final TextView boardCellText){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isCellSet(rowIndex,columnIndex)) {
                    boardState[rowIndex][columnIndex] = whosTurn;
                    if (whosTurn == 'X') {
                        boardCellText.setText(R.string.X);
                        whosTurn = 'O';
                    } else if (whosTurn == 'O') {
                        boardCellText.setText(R.string.O);
                        whosTurn = 'X';
                    }
                    if (updateGameStatus() == 0) {
                        tvWhosTurn.setText("Turn: " + whosTurn);
                    }
                    else if(updateGameStatus() == -1){
                        tvWhosTurn.setText("Game: Draw");
                        stopMatch();
                    }
                    else{
                        tvWhosTurn.setText(whosTurn +" loses!");
                        stopMatch();
                    }
                }
                else{
                    tvWhosTurn.setText(tvWhosTurn.getText()+" Please choose a Cell Which is not already Occupied");
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
