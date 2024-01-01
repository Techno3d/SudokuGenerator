public class SudokuGen {
    public static void main(String[] args) {
        int[][] board = sortMethod(generate());
        print9x9(board);
        System.out.println(validate(board));
        int counter = 0;
        int total = 50;
        for (int i = 0; i < total; i++) {
            counter += validate(sortMethod(generate())) ? 1 : 0;
        }
        if(total>0)
            System.out.println(counter + " out of " + total);
    }

    public static void print9x9(int[][] board) {
        int countRow = 1;
        System.out.println("--+---+---++---+---+---++---+---+---++");
        System.out.println("--+---+---++---+---+---++---+---+---++");
        for(int[] r : board) {
            int counter = 1;
            for(int c : r) {
                if(counter % 3 == 0) {
                    System.out.print(c + " || ");
                } else {
                    System.out.print(c + " | ");
                }
                counter++;
            }
            System.out.println("\n--+---+---++---+---+---++---+---+---++");
            if(countRow % 3 == 0)
                System.out.println("--+---+---++---+---+---++---+---+---++");
            countRow++;
        }
    }

    public static int[][] gen3x3() {
        int[][] board = new int[3][3];
        String list = "123456789";
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                int random = (int)(Math.random()*list.length());
                board[i][j] = ( (int)list.charAt(random) ) - 48;
                list = list.substring(0, random) + list.substring(random+1);
            }
        }
        return board;
    }

    public static int[][] generate() {
        int[][] board = new int[9][9];
        // Oh no
        for(int i = 0; i < 3; i++) {
            for(int k = 0; k < 3; k++) {
                int[][] preGen = gen3x3();
                for(int r = 0; r < 3; r++) {
                    for(int c = 0; c < 3; c++) {
                        board[i*3+r][k*3+c] = preGen[r][c];
                    }
                }
            }
        }
        return board;
    }

    // Hopefully foolproof
    public static int[][] sortMethod(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            if((i+1)%3 == 2) 
                continue;
            Cache cache = new Cache();
            for (int j = 0; j < board[0].length; j++) {
                if(cache.contains(board[i][j])) {
                    swap(board, i, j, cache);
                }
                cache.addToCache(board[i][j]);
            }
        }

        // Sort by collumn
        // Can only change with adj numbers, so as to not mess up the sorted rows
        // Tested by hand to work, just hard to implement in code
        for (int i = 0; i < board.length; i++) {
            if((i+1)%3==0) // +1 is important
                continue; // Cannot sort with anything, against the edge of the 3x3
            //Could this have been a string field? yes, i just forgot about that until it was too late
            Cache cache = new Cache(); // Cursed class that is basically a wrapper around string so that i can modify it from a different function
            for (int j = 0; j < board[0].length; j++) {
                if(cache.contains(board[j][i])) {
                    int prev = swapC(board, j, i, cache, -1);
                    if(prev != -1) {
                        handleStupidSwap(board, cache, prev, i);
                    }
                }
                cache.addToCache(board[j][i]);
            }
        }
        return board;
    }

    // If we do a stupid swap, then that leads to another stupid swap, it should re run the code for the nested stupid swaps
    public static void handleStupidSwap(int[][] board, Cache cache, int prev, int i) {
        for (int k = 0; k < board.length; k++) {
            if(k==prev) {
                continue;
            }
            if(board[prev][i]==board[k][i]) {
                int a = swapC(board, k, i, cache, prev);
                if(a != -1)
                    handleStupidSwap(board, cache, a, i); // ig more recursion
            }
        }
    }

    public static int swapC(int[][] board, int j, int i, Cache cache, int skip) {
        int temp = board[j][i];
        int c = i+1; // The column next to the loc
        if(i%3==0 && Math.random() > 0.5) { // fixes one of the most annoying runtime errors (stack overflow)
            c++;
        }
        int originalC = c;
        int idk = -1;
        while(cache.contains(board[j][c])) {
            // if it leaves 3x3, we have to go back, and change something
            if((c+1)/3*3 != i/3*3) {
                for (int k = 0; k < board.length; k++) {
                    if(board[k][i] == board[j][i]) {
                        if(k==skip) { // Stops the function from trying to re replace the replace to what it was before just for it to replace the re replace with the replace
                            continue;
                        }
                        // Oh shoot infinite loop, gotta just do a stupid swap then redo the entire col
                        if(k==j) { // No number before this one that we can replace with, so we just do stupid swap
                            //System.out.println(i + " " + k + " " + board[j][i] + " " + j);
                            c = originalC; // Do the stupid swap
                            idk = j; // Gotta return it so that we skip this on the reiter
                            break; // And then break out of the loop
                        } else {
                            // Ig this is recursion
                            return swapC(board, k, i, cache, skip); // Could return true if it runs into infi loop later
                        }
                    }
                }
                break; // Break out of the loop if incrementing c would start swapping outside its board
            }
            c++;
        }
        board[j][i] = board[j][c];
        board[j][c] = temp;
        cache.addToCache(board[j][i]); // If we have went back, add stuff to the cache
        return idk; // Return idk
    }

    public static void swap(int[][] board, int i, int j, Cache cache) {
        int temp = board[i][j];
        // First positions in the smaller grid
        int r = (i/3)*3 + 1; //But not in the row we are checking
        int c = (j/3)*3;
        boolean cry = false;
        while(cache.contains(board[r][c])) {
            //Leaves the board
            if((c+1)/3*3 != j/3*3) {
                //Leaves the board
                if(r==(i/3)*3+2) {
                    for (int k = 0; k < board[i].length; k++) {
                        if(board[i][k] == board[i][j]) {
                            swap(board, i, k, cache);
                            cry = true;
                            break;
                        }
                    }
                    break;
                }
                r++;
                //Go back to the '0'th pos for the 3x3
                c = (j/3)*3;
            } else {
                c++;
            }
        }
        if(cry)
            return;
        board[i][j] = board[r][c];
        board[r][c] = temp;
        cache.addToCache(board[i][j]);
    }


    public static boolean validate(int[][] board) {
        boolean temp = true;
        for (int i = 0; i < board.length; i++) {
            String cache = "";
            String cache2 = "";
            for (int j = 0; j < board[0].length; j++) {
                if(cache.contains(""+board[i][j])) {
                    temp = false;
                }
                if(cache2.contains(""+board[j][i])) {
                    temp = false;
                }
                cache+= board[i][j];
                cache2+= board[j][i];
            }
        }
        return temp;
    }
}
