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

    private int[] gridValueTotalColumn;
    private int[][] gridActiveValueMatrix;
    private int[][] gridValueMatrix;
    private int[][] gridColorMatrix;

    public GameModel() {
        activePieceIndex = -1;
        previewPieceIndex = -1;
        activePieceX = -1;
        activePieceY = -1;
        this.gridValueTotalColumn = new int[gridRowCount];
        this.gridActiveValueMatrix = new int[gridRowCount][gridColCount];
        this.gridValueMatrix = new int[gridRowCount][gridColCount];
        this.gridColorMatrix = new int[gridRowCount][gridColCount];
    }

    public void updateGridValueMatrix() {
        Log.d("NRA", "GameModel.updateGridValueMatrix");
        try {
           for (int i = 0; i < gridColCount; i++) {
                for (int j = 0; j < gridRowCount; j++) {
                    if (gridActiveValueMatrix[j][i]>0) {
                        gridValueMatrix[j][i] = gridActiveValueMatrix[j][i];
                     }
                }
            }
            gridValueTotalColumn = updateGridValueTotalColumn();
            Log.v("NRA", "GameModel.updateGridValueMatrix()\n" + toString());
        }
        catch (Exception e) {
            Log.e("NRA", "GameModel.updateGridValueMatrix() failed"+e.getMessage());
        }
    }

    public int getGridRowCount() {
        return gridRowCount;
    }

    public int getGridColCount() {
        return gridColCount;
    }

    public int[] getGridValueTotalColumn() {
        Log.v("NRA", "GameModel.getGridValueTotalColumn");
            if(gridValueTotalColumn == null) {
                return updateGridValueTotalColumn();
            } else {
                return gridValueTotalColumn;
            }
    }

    public int[] updateGridValueTotalColumn() {
        Log.v("NRA", "getGridValueTotalColumn");
        try {
               for (int i = 0; i < gridColCount; i++) {
                gridValueTotalColumn[i] = 0;
                for (int j = 0; j < gridRowCount; j++) {
                    gridValueTotalColumn[i] += gridValueMatrix[gridRowCount - j - 1][i];
                    //    if (gridValueTotalColumn[i] > 0 && gridValueMatrix[i][gridRowCount - j - 2] == 0) {
                    //        break;
                    //    }
                }
            }
        }
        catch (Exception e) {
            Log.e("NRA", "GameModel.updateGridValueTotalColumn() failed: " + e.getMessage());;
        }
        return gridValueTotalColumn;
    }

    public void updateGridActiveValueMatrix(Piece activePiece) {
        Log.v("NRA", "GameModel.updateGridActiveValueMatrix(Piece activePiece)");
        resetGridActiveValueMatrix();
        try {
            int x = activePiece.getX();
            int y = activePiece.getY();
            int dim = activePiece.getDim();
            for (int i = 0; i < gridColCount; i++) {
                for (int j = 0; j < gridRowCount; j++) {
                    if (i >= x && i < x + dim && j >= y && j < y + dim) {
                        int u = x - i + dim - 1;
                        int v = y - j + dim - 1;
                        gridActiveValueMatrix[j][i] = activePiece.getPatternNum()[dim - 1 - v][dim - 1  - u];
                    }
                }
            }
            Log.v("NRA", toString());
        }
        catch (Exception e) {
            Log.e("NRA", "GameModel.updateGridActiveValueMatrix() failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int[] getGridValueColumn(int colIndex) {
        return getColumn(gridValueMatrix, colIndex);
    }

    public void resetGridActiveValueMatrix() {
        try {
            for (int i = 0; i < gridColCount; i++) {
                for (int j = 0; j < gridRowCount; j++) {
                    gridActiveValueMatrix[j][i]=0;
                }
            }
        }
        catch (Exception e) {
            Log.e("NRA", "GameModel.resetGridActiveValueMatrix() failed");
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
        String str = "GameModel.toString()\n";
        str += "activePieceIndex=" + activePieceIndex + "\n";
        str += "activePieceX=" + activePieceX + "\n";
        str += "activePieceY=" + activePieceY + "\n";
        try {
//            str += "gridActiveValueMatrix=\n";
//            for (int i = 0; i < gridRowCount; i++) {
//                str += "\t";
//                for (int j = 0; j < gridColCount; j++) {
//                    str += gridActiveValueMatrix[i][j] + " ";
//                }
//                str += "\n";
//            }
            str += "gridValueMatrix=\n";
            for (int i = 0; i < gridRowCount; i++) {
                str += "\t";
                for (int j = 0; j < gridColCount; j++) {
                    str += gridValueMatrix[i][j] + " ";
                }
                str += "\n";
            }
            str += "gridValueTotalColumn=\n";
            if (gridValueTotalColumn!=null) {
                str += "\t";
                for (int i = 0; i < gridColCount; i++) {
                    str += gridValueTotalColumn[i] + " ";
                }
            } else {
                str +="null";
            }

        }
        catch (Exception e) {
            Log.d("NRA", "GameModel.toString() failed");
        }
        return str;
    }

    public static int[] getColumn(int[][] matrix, int index){
        int[] column = new int[matrix[0].length];
        for(int i=0; i<column.length; i++){
            column[i] = matrix[i][index];
        }
        return column;
    }
}
