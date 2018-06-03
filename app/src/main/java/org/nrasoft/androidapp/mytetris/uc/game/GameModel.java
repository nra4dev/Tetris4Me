package org.nrasoft.androidapp.mytetris.uc.game;

import android.util.Log;

import org.nrasoft.androidapp.mytetris.uc.game.pieces.Piece;


public class GameModel {
    private int gridRowCount=18;
    private int gridColCount=10;

    private int activePieceIndex;
    private int activePieceX;
    private int activePieceY;
    private int previewPieceIndex;

    private int[][] gridActiveValueMatrix;
    private int[][] gridValueMatrix;
    private int[][] gridColorMatrix;

    public GameModel() {
        activePieceIndex = -1;
        previewPieceIndex = -1;
        activePieceX = -1;
        activePieceY = -1;
        this.gridActiveValueMatrix = new int[gridRowCount][gridColCount];
        this.gridValueMatrix = new int[gridRowCount][gridColCount];
        this.gridColorMatrix = new int[gridRowCount][gridColCount];
    }

    public void updateGridValueMatrix() {
        Log.d("NRA", "cycleGridValueMatrix");
        try {
           for (int i = 0; i < gridColCount; i++) {
                for (int j = 0; j < gridRowCount; j++) {
                    if (gridActiveValueMatrix[j][i]>0) {
                        gridValueMatrix[j][i] = gridActiveValueMatrix[j][i];
                    }
                }
            }
            Log.d("NRA", toString());
        }
        catch (Exception e) {
            Log.d("NRA", "GameModel.cycleGridValueMatrix() failed");
        }
    }

    public void updateGridActiveValueMatrix(Piece activePiece) {
        try {
            int x = activePiece.getX();
            int y = activePiece.getY();
            for (int i = 0; i < gridColCount; i++) {

                for (int j = 0; j < gridRowCount; j++) {
                    if (i >= x && i < x + 4 && j >= y && j < y + 4) {
                        int u = x - i + 3;
                        int v = y - j + 3;
                        Log.v("NRA", "gridActiveValueMatrix[" + j + "][" + i + "]=activePiece[" + (3 - v) + "][" + (3 - u) + "]=" + activePiece.getPatternNum()[3 - v][3 - u]);
                        gridActiveValueMatrix[j][i] = activePiece.getPatternNum()[3 - v][3 - u];
                    } else {
                        gridActiveValueMatrix[j][i]=0;
                    }
                }
            }
        }
        catch (Exception e) {
            Log.d("NRA", "GameModel.updateGridValues() failed");
        }
    }
    public int getActivePieceIndex() {
        return activePieceIndex;
    }

    public void setActivePieceIndex(int activePieceIndex) {
        this.activePieceIndex = activePieceIndex;
    }

    public int getActivePieceX() {
        return activePieceX;
    }

    public void setActivePieceX(int activePieceX) {
        this.activePieceX = activePieceX;
    }

    public int getActivePieceY() {
        return activePieceY;
    }

    public void setActivePieceY(int activePieceY) {
        this.activePieceY = activePieceY;
    }

    public int getPreviewPieceIndex() {
        return previewPieceIndex;
    }

    public void setPreviewPieceIndex(int previewPieceIndex) {
        this.previewPieceIndex = previewPieceIndex;
    }

    public int[][] getGridValueMatrix() {
        return gridValueMatrix;
    }

    public void setGridValue(int row, int col, int value) {
        this.gridValueMatrix[row][col] = value;
    }

    public int[][] getGridColorMatrix() {
        return gridColorMatrix;
    }

    public void setGridColorMatrix(int[][] gridColorMatrix) {
        this.gridColorMatrix = gridColorMatrix;
    }

    public String toString() {
        String str = "GameModel\n";
        str += "activePieceIndex=" + activePieceIndex + "\n";
        str += "activePieceX=" + activePieceX + "\n";
        str += "activePieceY=" + activePieceY + "\n";
        try {
            str += "gridActiveValueMatrix=\n";
            for (int i = 0; i < gridRowCount; i++) {
                str += "\t";
                for (int j = 0; j < gridColCount; j++) {
                    str += gridActiveValueMatrix[i][j] + " ";
                }
                str += "\n";
            }
            str += "gridValueMatrix=\n";
            for (int i = 0; i < gridRowCount; i++) {
                str += "\t";
                for (int j = 0; j < gridColCount; j++) {
                    str += gridValueMatrix[i][j] + " ";
                }
                str += "\n";
            }
        }
        catch (Exception e) {
            Log.d("NRA", "GameModel.toString() failed");
        }
        return str;
    }
}
