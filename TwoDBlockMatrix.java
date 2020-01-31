import java.io.*;
import java.util.*;
import java.text.*;

public class TwoDBlockMatrix{

    public float[][] matrix;   
    
    TwoDBlockMatrix(float[][] array){
        matrix = new float[array.length][array[0].length];
        for(int i = 0; i<array.length; i++){
            for(int j = 0; j<array[0].length;j++){
                matrix[i][j] = array[i][j];
            }
        }
    }
    
    TwoDBlockMatrix transpose(){
        int row = matrix.length;
        int col = matrix[0].length;
        float[][] temp = new float[col][row];
        for(int i = 0; i<row; i++){
            for(int j = 0; j<col; j++){
                temp[j][i] = matrix[i][j];
            }
        }
        TwoDBlockMatrix trMatrix = new TwoDBlockMatrix(temp);
        return trMatrix;
    }

    TwoDBlockMatrix getSubBlock(int row_start, int col_start, int row_end, int col_end)throws SubBlockNotFoundException{
        if((row_start < 1 || row_start >matrix.length) || (row_end < 1 || row_end >matrix.length) ||(col_start < 1 || col_start >matrix[0].length) ||(col_end < 1 || col_end >matrix[0].length)){
            throw new SubBlockNotFoundException("SubBlockNotFoundException");
        }
        float[][] arr = new float[row_end-row_start+1][col_end-col_start+1];
        for(int i = row_start-1; i<row_end-1; i++){
            for(int j = col_start-1; j<col_end-1; j++){
                arr[i-row_start+1][j-col_start+1] = matrix[i][j];
            }
        }
        TwoDBlockMatrix sub = new TwoDBlockMatrix(arr);
        return sub;
    }

    boolean checkFloat(float num){
        boolean isFloat = true;
        int numInt = (int)num;
        float numFloat = numInt;
        if(num - numFloat == 0.00){ 
            isFloat = false;
        } 
        return isFloat;
    }

    String simpleString(String s){
        String out = ""; 
        for(int i = 0; i<s.length(); i++){
            if(s.charAt(i) != '\n'){
                out+=s.charAt(i);
            }
        }
        return out;
    }

    @Override
    public String toString(){
        DecimalFormat formatToDeci = new DecimalFormat("0.00");
        String out = "";
        float[][] copy = new float[matrix.length][matrix[0].length];
        for(int i = 0; i<matrix.length; i++){
            for(int j = 0; j<matrix[0].length; j++){
                copy[i][j] = matrix[i][j];
            }
        }
        for(int i = 0; i<copy.length; i++){
            for(int j = 0; j<copy[0].length; j++){
                if(copy[i][j] != 0){
                    out+="#\n";
                    out+=String.valueOf(i+1)+" "+String.valueOf(j+1)+"\n";
                    int col=0;
                    for(int tempCol = j; tempCol<copy[0].length; tempCol++){
                        if(copy[i][tempCol] == 0){
                            out = out.substring(0, out.length()-1)+";\n";
                            break;
                        }
                        else{
                            if(tempCol != copy[0].length-1){
                                if(checkFloat((float)copy[i][tempCol]) == true)
                                    out+=(formatToDeci.format(copy[i][tempCol]))+" ";
                                else
                                    out+=String.valueOf((int)copy[i][tempCol])+" ";
                            }
                            else{
                                if(checkFloat((float)copy[i][tempCol]) == true)
                                    out+=(formatToDeci.format(copy[i][tempCol]))+";\n";
                                else
                                out+=String.valueOf((int)copy[i][tempCol])+";\n";
                            }
                            col++;
                            copy[i][tempCol] =0;
                        }
                    }   
                    String temp = "";
                    outer: for(int tempRow = i+1; tempRow<copy.length; tempRow++){
                        for(int tempCol = j; tempCol<j+col; tempCol++){
                            if(copy[tempRow][tempCol] != 0){
                                if(checkFloat((float)copy[tempRow][tempCol]) == true)
                                    temp+=(formatToDeci.format(copy[tempRow][tempCol]))+" ";
                                else
                                    temp+=String.valueOf((int)copy[tempRow][tempCol])+" ";
                                if(tempCol == j+col-1){
                                    for(int d = j; d<j+col; d++){
                                        copy[tempRow][d] = 0;
                                    }
                                }
                            }
                            else{
                                break outer;
                            }
                        }
                        if(temp!= ""){
                            out+=temp.substring(0, temp.length()-1)+";\n";
                            temp="";
                        }
                    }
                }
            }
        }
        if(out.length() != 0)
            out=out.substring(2)+"#";
        return out;
    }

    TwoDBlockMatrix multiply(TwoDBlockMatrix other)throws IncompatibleDimensionException{
        int matCol = matrix[0].length;
        int othRow = other.matrix.length;
        if(matCol != othRow){
            throw new IncompatibleDimensionException("IncompatibleDimensionException");
        }
        else{
            float array[][] = new float[matrix.length][other.matrix[0].length];
            for(int i = 0; i<array.length; i++){
                for(int j = 0; j<array[0].length; j++){
                    array[i][j] = 0;
                    for(int k = 0; k<matCol; k++){
                        array[i][j] += matrix[i][k]*other.matrix[k][j];
                    }
                }
            }

            TwoDBlockMatrix out = new TwoDBlockMatrix(array);
            return out;
        }
    }

    float[][] deleteRowAndCol(float[][] array, int row, int col){
        float[][] out = new float[array.length-1][array.length-1];
        int p = 0;
        for(int i = 0; i<array.length; i++){
            if(i == row)
                continue;
            int q =0;
            for(int j = 0; j<array.length; j++){
                if(j!= col){
                    out[p][q] = array[i][j];
                    q++;
                }
            }
            p++;
        }
        return out;
    }

    float determinant(float[][] array){
        if(array.length == 2){
            return((array[0][0]*array[1][1]) - (array[0][1]*array[1][0]));
        }
        else{
            float det = 0;
            for(int i = 0; i<array.length; i++){
                det += (Math.pow(-1, i)*array[0][i]*determinant(deleteRowAndCol(array, 0, i)));
            }
            return det;
        }
    }

    TwoDBlockMatrix inverse()throws InverseDoesNotExistException{
        if(matrix.length != matrix[0].length){
            throw new InverseDoesNotExistException("InverseDoesNotExistException");
        }
        else if(matrix.length == matrix[0].length && determinant(matrix)==0){
            throw new InverseDoesNotExistException("InverseDoesNotExistException");
        }
        else{
            float det = (float)determinant(matrix);
            float[][] cofactorMatrix = new float[matrix.length][matrix.length];
            for(int i = 0; i<matrix.length; i++){
                for(int j = 0; j<matrix[0].length; j++){
                    cofactorMatrix[i][j] = (float)(Math.pow(-1, (i+j))*determinant(deleteRowAndCol(matrix, i, j)))/det;
                }
            }
            TwoDBlockMatrix invr = new TwoDBlockMatrix(cofactorMatrix);
            return invr.transpose();
        }
    }

    static TwoDBlockMatrix buildTwoDBlockMatrix(InputStream in) throws IOException{
        String input = "";
        int data = in.read();
        while(data != -1){
            input += String.valueOf((char)data);
            data = in.read();
        }
        in.close();
        int startBl[] = {0,0};
        int endBl[] = {0,0};
        int dim[] = {0,0};
        String temp = "";
        int i = 0;
        while(i<input.length()){
            if(input.charAt(i) == '\n'){
                i++;
                continue;
            }
            int tCol = 1;
            endBl[0] = 0;
            endBl[1] = 0;
            int j = 0;
            while(i<input.length()){
                    if(input.charAt(i) == ' '){
                        startBl[j] = Integer.valueOf(temp);
                        temp = "";
                        j++;
                        i++;
                    }
                    else if(input.charAt(i) == '\n'){
                        startBl[j] = Integer.valueOf(temp);
                        temp = "";
                        i++;
                        break;
                    }
                    temp+=Character.toString(input.charAt(i));
                    i++;
            }
            while(i<input.length()){
                if(input.charAt(i) == '#'){
                    break;
                }
                if(input.charAt(i) == '\n'){
                    i++;
                    continue;
                }
                while(i<input.length()){
                    if(input.charAt(i) == ';'){
                        if(tCol>endBl[1])
                            endBl[1] = tCol;
                        endBl[0]++;
                        tCol = 0;
                        i++;
                        break;
                    }
                    else if(input.charAt(i) == ' '){
                        tCol++;
                        i++;
                        continue;
                    }
                    else{
                        i++;
                    }
                }
            }
            endBl[0] = endBl[0] + startBl[0] -1;
            endBl[1] = endBl[1] + startBl[1] -1;
            if(dim[0] < endBl[0])
                dim[0] = endBl[0];
            if(dim[1] < endBl[1])
                dim[1] = endBl[1];
            i++;
        }
        float array[][] = new float[dim[0]][dim[1]];
        for(int k = 0; i<array.length; i++){
            for(int l = 0; l<array[0].length; l++){
                array[k][l] = 0;
            }
        }
        i = 0;
        while(i<input.length()){
            int row = 0;
            int col = 0;
            int j = 0;
            if(input.charAt(i) == '\n'){
                i++;
                continue;
            }
            while(i<input.length()){
                if(input.charAt(i) == ' '){
                    startBl[j] = Integer.valueOf(temp);
                    temp = "";
                    j++;
                    i++;
                }
                else if(input.charAt(i) == '\n'){
                    startBl[j] = Integer.valueOf(temp);
                    temp = "";
                    i++;
                    break;
                }
                temp+=Character.toString(input.charAt(i));
                i++;
            }
            row = startBl[0]-1;
            col = startBl[1]-1;
            while(i<input.length()){
                if(input.charAt(i) == '#'){
                    temp = "";
                    i++;
                    break;
                }
                if(input.charAt(i) == '\n'){
                    i++;
                    temp = "";
                    continue;
                }
                while(i<input.length()){
                    if(input.charAt(i) == ';'){
                        array[row][col] = Integer.valueOf(temp);
                        row++;
                        col = startBl[1]-1;
                        temp = "";
                        i++;
                        break;
                    }
                    else if(input.charAt(i) == ' '){
                        array[row][col] = Integer.valueOf(temp);
                        temp = "";
                        col++;
                        i++;
                    }
                    else{
                        temp+=String.valueOf(input.charAt(i));
                        i++;
                    }
                }
            }
        }
        TwoDBlockMatrix t1 = new TwoDBlockMatrix(array);
        return t1;
    }

}