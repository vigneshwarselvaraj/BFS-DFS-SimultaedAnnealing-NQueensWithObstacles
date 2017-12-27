import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class homework {
    private static int num = 0;
    private static int numOfLizards = 0;
    private static int totalBoxes;
    private long start = System.currentTimeMillis();

    public static void main(String[] args) {
        String fileName = "input.txt";
        String line;
        String searchMethod;
        homework hw = new homework();


        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            searchMethod = line;
            line = bufferedReader.readLine();
            num = Integer.parseInt(line);
            totalBoxes = num*num;
            line = bufferedReader.readLine();
            numOfLizards = Integer.parseInt(line);
            int[][] resMatrix = new int[num][num];
            int[][] resultArray = new int[num][num];

            for(int i =0;i<num;i++)
            {
                line = bufferedReader.readLine();
                char[] arr = line.toCharArray();
                for(int j=0;j<num;j++)
                {
                    if(arr[j]=='2')
                        resMatrix[i][j]=2;
                    else
                        resMatrix[i][j]=0;
                }

            }

            bufferedReader.close();
            switch (searchMethod) {
                case "DFS":
                    resultArray = hw.DepthFirstSearch(resMatrix, 0, 0, 0, 0);
                    hw.printSolution(resultArray);
                    break;
                case "BFS": {
                    short[][] shortResMatrix = new short[num][num];
                    for (int i = 0; i < num; i++)
                        for (int j = 0; j < num; j++)
                            shortResMatrix[i][j] = (short) resMatrix[i][j];

                    shortResMatrix = hw.bfsSearch(shortResMatrix, num, numOfLizards);
                    if(shortResMatrix!=null) {
                        for (int i = 0; i < num; i++)
                            for (int j = 0; j < num; j++)
                                resultArray[i][j] = (int) shortResMatrix[i][j];
                    }
                    else
                        resultArray = null;
                    hw.printSolution(resultArray);
                    break;
                }
                case "SA": {
                    short[][] shortResMatrix = new short[num][num];
                    for (int i = 0; i < num; i++)
                        for (int j = 0; j < num; j++)
                            shortResMatrix[i][j] = (short) resMatrix[i][j];

                    shortResMatrix = hw.saSearch(shortResMatrix, num, numOfLizards);
                    if(shortResMatrix!=null){
                        for (int i = 0; i < num; i++)
                            for (int j = 0; j < num; j++)
                                resultArray[i][j] = (int) shortResMatrix[i][j];
                    }
                    else
                        resultArray = null;
                    hw.printSolution(resultArray);
                    break;
                }
            }

        }
        catch(Exception ex) {
            System.out.println(
                    "FAIL" + ex.getMessage() );
        }

    }


    private void printSolution(int[][] printArray){
        try {
            File f = new File("output.txt");
            FileOutputStream fos;
            fos = new FileOutputStream(f);
            PrintWriter pw = new PrintWriter(fos);
            if(printArray != null){
                //System.out.println("OK");
                pw.write("OK\n");
                for(int i=0;i<num;i++)
                {
                    for(int j=0;j<num;j++)
                    {
                        if(printArray[i][j]<=0) pw.write("0");
                        else
                        {
                            String str = String.valueOf(printArray[i][j]);
                            pw.write(str);
                        }
                    }
                    pw.write("\n");
                }
            }
            else{
                pw.write("FAIL\n");
            }
            System.out.println(System.currentTimeMillis() - start);
            pw.flush();
            fos.close();
            pw.close();
        }

        catch(Exception ex) {
            System.out.println(
                    "FILE WRITE FAIL" );
        }

        /*for(int i=0; i<num; i++) {
            for(int j = 0; j<num; j++)
                if(printArray[i][j]<=0)
                    System.out.print("0\t");
                else
                    System.out.print(printArray[i][j]+"\t");
            System.out.println("");
        }

        System.out.println("Time is "+ (System.currentTimeMillis()-start));*/
        //System.exit(0);
    }

    private int[][] DepthFirstSearch(int[][] testMatrix, int row, int column, int currlizardCount, int invalidCount){
        if((System.currentTimeMillis()-start) > 280000)
            return null;
        for(int i = row; i<num; i++) {
            for (int j = 0; j<num; j++) {
                if(i==row && j< column )
                    j = column;

                if((testMatrix[i][j] == 0)) {
                    testMatrix[i][j] = 1;
                    currlizardCount = currlizardCount+1;

                    if(currlizardCount == numOfLizards) {
                        return testMatrix;
                    }


                    invalidCount += setInvalids(testMatrix, i, j);
                    if((totalBoxes-invalidCount) < (numOfLizards-currlizardCount)){
                        currlizardCount = currlizardCount-1;
                        invalidCount -= resetInvalids(testMatrix, i, j);
                        testMatrix[i][j] = 0;
                        return null;
                    }

                    int[][] returnArray = DepthFirstSearch(testMatrix, i, j, currlizardCount, invalidCount);

                    if(returnArray!=null)
                        return returnArray;

                    currlizardCount = currlizardCount-1;
                    invalidCount -= resetInvalids(testMatrix, i, j);
                    if(testMatrix[i][j] !=2 )
                        testMatrix[i][j] = 0;

                }

            }
        }
        return null;
    }



    private int setInvalids(int[][] setArray, int row, int column){
        //set all other boxes in same row as invalid
        int count = 0;
        for(int i = column+1; i< num; i++){
            if(setArray[row][i] == 2)
                break;
            else
            if(setArray[row][i]==0)
                count++;
            if(setArray[row][i] <= 0)
                setArray[row][i] -= 1;
        }

        for(int i = column-1; i>= 0; i--){
            if(setArray[row][i] == 2)
                break;
            else
            if(setArray[row][i]==0)
                count++;
            if(setArray[row][i] <= 0)
                setArray[row][i] -= 1;
        }


        //set all other boxes in same column as invalid
        for(int i = row+1; i<num; i++) {
            if(setArray[i][column] == 2)
                break;
            else
            if(setArray[i][column]==0)
                count++;
            if (setArray[i][column] <= 0)
                setArray[i][column] -= 1;
        }

        for(int i = row-1; i>=0; i--) {
            if(setArray[i][column] == 2)
                break;
            else
            if(setArray[i][column]==0)
                count++;
            if (setArray[i][column] <= 0)
                setArray[i][column] -= 1;
        }

        //set all diagonal boxes above and left invalid
        for(int i = row-1, j = column-1; i>=0 && j>=0; i--, j--) {
            if(setArray[i][j] == 2)
                break;
            else
            if(setArray[i][j]==0)
                count++;
            if (setArray[i][j] <= 0)
                setArray[i][j] -=1 ;
        }

        //set all diagonal boxes below and right invalid
        for(int i = row+1, j = column+1; i<num && j<num; i++, j++) {
            if(setArray[i][j] == 2)
                break;
            else
            if(setArray[i][j]==0)
                count++;
            if (setArray[i][j] <= 0)
                setArray[i][j] -=1 ;
        }

        for(int i = row+1, j = column-1 ; i < num && j>=0; i++, j--) {
            if(setArray[i][j] == 2)
                break;
            else
            if(setArray[i][j]==0)
                count++;
            if (setArray[i][j] <= 0)
                setArray[i][j] -= 1;
        }

        for(int i = row-1, j = column+1; i>=0 && j<num; i--, j++) {
            if(setArray[i][j] == 2)
                break;
            else
            if(setArray[i][j]==0)
                count++;
            if (setArray[i][j] <= 0)
                setArray[i][j] -= 1;
        }


        return count;
    }

    private int resetInvalids(int[][] setArray, int row, int column){
        //set all other boxes in same row as invalid
        int count = 0;

        for(int i = column+1; i< num; i++){
            if(setArray[row][i] == 2)
                break;
            else if(setArray[row][i]==-1 && i != column)
                count++;
            if(setArray[row][i] <= 0 && i != column)
                setArray[row][i] += 1;
        }
        //set all others in same row and to the left as invalid
        for(int i = column-1; i>= 0; i--){
            if(setArray[row][i] == 2)
                break;
            else if(setArray[row][i]==-1 && i != column)
                count++;
            if(setArray[row][i] <= 0 && i != column)
                setArray[row][i] +=1;
        }


        //set all others in same column and to the bottom as invalid
        for(int i = row+1; i<num; i++) {
            if(setArray[i][column] == 2)
                break;
            else if(setArray[i][column]==-1 && i!= row)
                count++;
            if (setArray[i][column] <= 0 && i!= row)
                setArray[i][column] +=1;
        }

        //set all others in same column and to the top as invalid
        for(int i = row-1; i>=0; i--) {
            if(setArray[i][column] == 2)
                break;
            else if(setArray[i][column]==-1 && i!= row)
                count++;
            if (setArray[i][column] <= 0 && i!= row)
                setArray[i][column] +=1;
        }



        //set all diagonal boxes above and left invalid
        for(int i = row-1, j = column-1; i>=0 && j>=0; i--, j--) {
            if(setArray[i][j] == 2)
                break;
            else
            if(setArray[i][j] == -1)
                count++;
            if (setArray[i][j] <= 0)
                setArray[i][j] +=1 ;
        }

        //set all diagonal boxes below and right invalid
        for(int i = row+1, j = column+1; i<num && j<num; i++, j++) {
            if(setArray[i][j] == 2)
                break;
            else
            if(setArray[i][j] == -1)
                count++;
            if (setArray[i][j] <= 0)
                setArray[i][j] +=1 ;
        }

        for(int i = row+1, j = column-1 ; i < num && j>=0; i++, j--) {
            if(setArray[i][j] == 2)
                break;
            else
            if(setArray[i][j]==-1)
                count++;
            if (setArray[i][j] <= 0)
                setArray[i][j] += 1;
        }

        for(int i = row-1, j = column+1; i>=0 && j<num; i--, j++) {
            if(setArray[i][j] == 2)
                break;
            else
            if(setArray[i][j] == -1)
                count++;
            if (setArray[i][j] <= 0)
                setArray[i][j] += 1;
        }
        return count;
    }



    // below code is for bfs implementation

    private short[][] bfsSearch(short[][] resMatrix, int num, int numOfLizards) {
        Queue<Node> bfsQueue = new LinkedList<>();
        int size = 0;
        for (short i = 0; i < num; i++) {
            for (short j = 0; j < num; j++) {
                if (resMatrix[i][j] == 0) {
                    Node newNode = new Node(i,j,(short) 1);
                    newNode.parent = null;
                    bfsQueue.add(newNode);
                }
            }
        }

        while (!(bfsQueue.isEmpty()) && (System.currentTimeMillis()-start) < 280000) {
            if(bfsQueue.size()>size)
                size = bfsQueue.size();
            Node pollNode = bfsQueue.poll();
            short currLength = pollNode.level;
            if (currLength == numOfLizards) {
                while(pollNode.parent!=null){
                    resMatrix[pollNode.row][pollNode.column] = 1;
                    pollNode = pollNode.parent;
                }
                resMatrix[pollNode.row][pollNode.column] = 1;
                return resMatrix;
            }

            ArrayList<Node> nextValidPositions = checkNextValidPositions(resMatrix, num, pollNode);

            short numberOfNextNodes = (short)nextValidPositions.size();
            for(short i=0; i<numberOfNextNodes; i++){
                Node newNode = nextValidPositions.get(i);
                newNode.parent = pollNode;
                bfsQueue.add(newNode);
            }
        }
        return null;
    }

    private ArrayList<Node> checkNextValidPositions(short[][] resMatrix, int num, Node pollNode) {
        ArrayList<Node> positions = new ArrayList<>();
        for (short i = pollNode.row; i < num; i++){
            for (short j = 0; j < num; j++){
                if (i==pollNode.row && j<pollNode.column)
                    j=pollNode.column;
                if(resMatrix[i][j] == 0)
                    if(isValid(resMatrix, i, j, pollNode)){
                        Node newNode = new Node(i, j, (short) (pollNode.level+1));
                        if(isAnyValidChild(resMatrix, num, newNode))
                            positions.add(newNode);
                        else if(newNode.level == numOfLizards)
                            positions.add(newNode);
                    }
            }
        }
        return positions;
    }

    private boolean isAnyValidChild(short[][] resMatrix, int num, Node pollNode) {
        for (short i = pollNode.row; i < num; i++){
            for (short j = 0; j < num; j++){
                if (i==pollNode.row && j<pollNode.column)
                    j=pollNode.column;
                if(resMatrix[i][j] == 0)
                    if(isValid(resMatrix, i, j, pollNode)){
                        return true;
                    }
            }
        }
        return false;
    }

    private boolean isValid(short[][] validateRes, int row, int column, Node pollNode) {
        boolean noParent = false;
        do{
            if(pollNode.row == row && pollNode.column == column)
                return false;
            else{
                int stateJRow = pollNode.row, stateJColumn = pollNode.column;
                boolean isTree = false; // initialising tree as false before checking row, column, diagonal trees

                if(row == stateJRow){
                    int checkTreeFrom, checkTreeTo;
                    if(column > stateJColumn){
                        checkTreeFrom = stateJColumn;
                        checkTreeTo = column;
                    }
                    else{
                        checkTreeFrom = column;
                        checkTreeTo = stateJColumn;
                    }
                    for(int k = (checkTreeFrom+1); k<checkTreeTo; k++){
                        if(validateRes[row][k] == 2)
                            isTree = true;
                    }
                    //increment conflict count if there are no trees in between
                    if(!isTree)
                        return false;
                }
                else if (column == stateJColumn){
                    int checkTreeFrom, checkTreeTo;
                    if(row > stateJRow){
                        checkTreeFrom = stateJRow;
                        checkTreeTo = row;
                    }
                    else{
                        checkTreeFrom = row;
                        checkTreeTo = stateJRow;
                    }
                    for(int k = (checkTreeFrom+1); k< checkTreeTo; k++){
                        if(validateRes[k][column] == 2)
                            isTree = true;
                    }
                    if(!isTree)
                        return false;
                }

                else if (Math.abs(row - stateJRow) == Math.abs(column - stateJColumn)){
                    int checkFromCol, checkToCol, checkFromRow, checkToRow;

                    //assign the lower column as checkFromCol, and the bigger column as checkToCol. Assign corresponding rows
                    if(column > stateJColumn){
                        checkFromCol = stateJColumn;
                        checkFromRow = stateJRow;
                        checkToCol = column;
                        checkToRow = row;
                    }
                    else{
                        checkFromCol = column;
                        checkFromRow = row;
                        checkToCol = stateJColumn;
                        checkToRow = stateJRow;
                    }

                    //verify if checkFromRow is bigger or smaller than checkToRow
                    if(checkFromRow > checkToRow) {
                        for(int k = checkFromRow-1, l = checkFromCol+1; k > checkToRow && l < checkToCol; k--, l++){
                            if(validateRes[k][l] == 2)
                                isTree = true;
                        }
                        if(!isTree)
                            return false;
                    }

                    else{
                        for(int k = checkFromRow+1, l = checkFromCol+1; k < checkToRow && l < checkToCol; k++, l++){
                            if(validateRes[k][l] == 2)
                                isTree = true;
                        }
                        if(!isTree)
                            return false;
                    }
                }
            }
            if(pollNode.parent == null)
                noParent = true;
            else
                pollNode = pollNode.parent;
        }while(!noParent);
        return true;
    }


    //Below code is for Simulated Annealing implementation

    private short[][] saSearch(short[][] resMatrix, int num, int numOfLizards){
        lizard[] currentState = setInitialState(resMatrix, num, numOfLizards);
        lizard[] nextState;
        int currConflicts=  countConflicts(currentState, resMatrix, numOfLizards);
        int nextConflicts;
        double conflictsChange, temperature = 150;
        double probability, randProbCheck;
        int iteration;
        for(iteration = 1; temperature > 0 && (System.currentTimeMillis()-start) < 280000; iteration++){
            nextState = getNextState(currentState, resMatrix, num, numOfLizards);
            nextConflicts = countConflicts(nextState, resMatrix, numOfLizards);

            conflictsChange = currConflicts - nextConflicts;
            probability = Math.exp(conflictsChange / temperature);
            randProbCheck = Math.random();

            if(currConflicts == 0){
                short[][] printMatrix = new short[num][num];
                for(int i=0; i<num; i++)
                    System.arraycopy(resMatrix[i], 0, printMatrix[i], 0, num);

                for(int i=0; i<numOfLizards; i++)
                    printMatrix[currentState[i].row][currentState[i].column] = 1;

                return printMatrix;
            }

            if(conflictsChange > 0){
                currentState = nextState;
                currConflicts = nextConflicts;
            }

            else if(probability > randProbCheck)
            {
                currentState = nextState;
                currConflicts = nextConflicts;
            }
            temperature =  (1/ (100 * Math.log(1+iteration)));
        }
        return null;
    }

    private lizard[] setInitialState(short[][] resMatrix, int num, int numOfLizards){
        Random randY = new Random();
        int rowPos = 0, colPos;
        lizard[] lizards = new lizard[numOfLizards];

        short[][] copyMatrix = new short[num][num];
        for(int i=0; i<num; i++)
            System.arraycopy(resMatrix[i], 0, copyMatrix[i], 0, num);

        for(int i=0; i< numOfLizards;){
            //rowPos = randX.nextInt(num);
            colPos = randY.nextInt(num);
            if(copyMatrix[rowPos][colPos] == 0){
                lizards[i] = new lizard(rowPos, colPos);
                i++;
            }

            if (rowPos == (num-1))
                rowPos = 0;
            else
                rowPos++;
        }
        return lizards;
    }

    private lizard[] getNextState(lizard[] currentState, short[][] resMatrix, int num, int numOfLizards){
        lizard[] nextState = new lizard[numOfLizards];
        Random rand = new Random();
        int changeLizard = rand.nextInt(numOfLizards);
        int rowPos, colPos, changed = 0;

        short[][] copyMatrix = new short[num][num];
        for(int i=0; i<num; i++)
            System.arraycopy(resMatrix[i], 0, copyMatrix[i], 0, num);

        for(int i=0; i<numOfLizards; i++){
            nextState[i] = new lizard(currentState[i].row, currentState[i].column);
            if(i == changeLizard){
                //rowPos = currentState[i].row;
                rowPos = rand.nextInt(num);
                colPos = rand.nextInt(num);
                while(changed==0){
                    if(copyMatrix[rowPos][colPos] == 0){
                        nextState[i].row = rowPos;
                        nextState[i].column = colPos;
                        changed = 1;
                    }
                    else{
                        rowPos = rand.nextInt(num);
                        colPos = rand.nextInt(num);
                    }
                }
            }
        }
        return nextState;
    }

    private int countConflicts(lizard[] state, short[][] resMatrix, int numOfLizards){
        int conflictCount = 0;
        boolean[] conflictFound = new boolean[numOfLizards];
        for(int i=0; i<numOfLizards; i++){
            for(int j=0; j<numOfLizards && !conflictFound[i] ; j++){
                if(i!=j){
                    //check if there are any trees between the lizards in same row
                    int stateIRow = state[i].row, stateIColumn = state[i].column;
                    int stateJRow = state[j].row, stateJColumn = state[j].column;
                    boolean isTree = false; // initialising tree as false before checking row, column, diagonal trees

                    if(stateIRow == stateJRow){
                        int checkTreeFrom, checkTreeTo;
                        if(stateIColumn > stateJColumn){
                            checkTreeFrom = stateJColumn;
                            checkTreeTo = stateIColumn;
                        }
                        else{
                            checkTreeFrom = stateIColumn;
                            checkTreeTo = stateJColumn;
                        }
                        for(int k = (checkTreeFrom+1); k<checkTreeTo; k++){
                            if(resMatrix[stateIRow][k] == 2)
                                isTree = true;
                        }
                        //increment conflict count if there are no trees in between
                        if(!isTree){
                            conflictCount+=2;
                            conflictFound[i] = true;
                            conflictFound[j] = true;
                        }
                    }
                    else if (stateIColumn == stateJColumn){
                        int checkTreeFrom, checkTreeTo;
                        if(stateIRow > stateJRow){
                            checkTreeFrom = stateJRow;
                            checkTreeTo = stateIRow;
                        }
                        else{
                            checkTreeFrom = stateIRow;
                            checkTreeTo = stateJRow;
                        }
                        for(int k = (checkTreeFrom+1); k< checkTreeTo; k++){
                            if(resMatrix[k][stateIColumn] == 2)
                                isTree = true;
                        }
                        if(!isTree){
                            conflictCount+=2;
                            conflictFound[i] = true;
                            conflictFound[j] = true;
                        }
                    }

                    else if (Math.abs(stateIRow - stateJRow) == Math.abs(stateIColumn - stateJColumn)){
                        int checkFromCol, checkToCol, checkFromRow, checkToRow;

                        //assign the lower column as checkFromCol, and the bigger column as checkToCol. Assign corresponding rows
                        if(stateIColumn > stateJColumn){
                            checkFromCol = stateJColumn;
                            checkFromRow = stateJRow;
                            checkToCol = stateIColumn;
                            checkToRow = stateIRow;
                        }
                        else{
                            checkFromCol = stateIColumn;
                            checkFromRow = stateIRow;
                            checkToCol = stateJColumn;
                            checkToRow = stateJRow;
                        }

                        //verify if checkFromRow is bigger or smaller than checkToRow
                        if(checkFromRow > checkToRow) {
                            for(int k = checkFromRow-1, l = checkFromCol+1; k > checkToRow && l < checkToCol; k--, l++){
                                if(resMatrix[k][l] == 2)
                                    isTree = true;
                            }
                            if(!isTree){
                                conflictCount+=2;
                                conflictFound[i] = true;
                                conflictFound[j] = true;
                            }
                        }

                        else{
                            for(int k = checkFromRow+1, l = checkFromCol+1; k < checkToRow && l < checkToCol; k++, l++){
                                if(resMatrix[k][l] == 2)
                                    isTree = true;
                            }
                            if(!isTree){
                                conflictCount+=2;
                                conflictFound[i] = true;
                                conflictFound[j] = true;
                            }
                        }
                    }
                }
            }
        }
        return conflictCount;
    }
}

//below class is for BFS
class Node {
    final short row;
    final short column;
    final short level;
    Node parent;

    Node(short x, short y, short lvl){
        row = x;
        column = y;
        level = lvl;
        parent = null;
    }
}


//below class is for Simulated Annealing
class lizard {
    int row, column;

    lizard(int xpos, int ypos){
        row = xpos;
        column = ypos;
    }
}
