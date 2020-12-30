import java.io.*;

/* 
 * Given a file with a n x n matrix of letters.
 * this project finds a word given in O(n) time
 * through the use of the KMP algorithm
*/

public class Puzzle
{
    char[][] puzzle;
    //
	// constructor: fn is the filename where the puzzle is stored
    // reads the file given and stores it in the puzzle array
	//
    public Puzzle(String fn) 
    {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(fn)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        int num;
        try {
            assert br != null;
            line = br.readLine();
            assert line != null;
            num = Integer.parseInt(line);
            puzzle = new char[num][num];
            for (int i = 0; i < num; i++) {
                line = br.readLine();
                for (int j = 0; j < num; j++) {
                    puzzle[i][j] = line.charAt(j);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    // search the puzzle for the given word
    // return {a, b, x, y} where (a, b) is
    // the starting location and (x, y) is 
    // the ending location
    // return null if the word can't be found
    // in the puzzle
    //
    public int[] search(String word) 
    {
    	int i,j; // The current position being checked for the word
    	int[] e; // The return array

        char[] w = new char[word.length()]; // word to array
        for (int k = 0; k < word.length(); k++) {
            w[k] = word.charAt(k);
        }
    	int[] f = failFunction(w); // failure function of word

    	int length = puzzle.length;
    	int word_length = word.length();

    	i=0; // check all possible options in first column
    	for (j = 0; j < length; j++) {
    	    e = right(w,f,i,j);
    	    if (e[0] != -1) {
    	        return e;
            }
    	    if (j <= length - word_length) {
    	        e = rd(w,f,i,j);
                if (e[0] != -1) {
                    return e;
                }
            }
            if (j >= word_length-1) {
                e = ru(w,f,i,j);
                if (e[0] != -1) {
                    return e;
                }
            }
        }

    	i= length-1; // check all possible options in last column
        for (j = 0; j < length; j++) {
            e = left(w,f,i,j);
            if (e[0] != -1) {
                return e;
            }
            if (j >= word_length-1) {
                e = lu(w,f,i,j);
                if (e[0] != -1) {
                    return e;
                }
            }
            if (j <= length-word_length) {
                e = ld(w,f,i,j);
                if (e[0] != -1) {
                    return e;
                }
            }
        }

    	j=0; // check all possible options in first row
        for (i = 0; i < length; i++) {
            e = down(w,f,i,j);
            if (e[0] != -1) {
                return e;
            }
            if (i <= length - word_length) {
                e = rd(w,f,i,j);
                if (e[0] != -1) {
                    return e;
                }
            }
            if (i >= word_length-1) {
                e = ld(w,f,i,j);
                if (e[0] != -1) {
                    return e;
                }
            }
        }

    	j= length-1; // check all possible options in last row
        for (i = 0; i < length; i++) {
            e = up(w,f,i,j);
            if (e[0] != -1) {
                return e;
            }
            if (i >= word_length-1) {
                e = lu(w,f,i,j);
                if (e[0] != -1) {
                    return e;
                }
            }
            if (i <= length-word_length) {
                e = ru(w,f,i,j);
                if (e[0] != -1) {
                    return e;
                }
            }
        }

    	return null;
    }

    // Failure function of the word
    public int[] failFunction(char[] word) {
        int[] f = new int[word.length];
        f[0] = 0;
        int j = 1;
        int len = 0;

        while (j < word.length) {
            if (word[j] == word[len]) {
                len++;
                f[j] = len;
                j++;
            } else {
                if (len != 0) {
                    len = f[len - 1];
                } else {
                    f[j] = len;
                    j++;
                }
            }
        }
        return f;
    }

    // KMP algorithm going to the right
    public int[] right(char[] word, int[] f, int i, int j) {
        int c = 0;
        int k = i;
        int p = j;
        int[] r = {-1,-1,-1,-1};

        while (k < puzzle.length) {
            if (word[c] == puzzle[k][p]) {
                if (r[0] == -1) {
                    r[0] = k;
                    r[1] = p;
                }
                c++;
                if (c == word.length) {
                    r[2] = k;
                    r[3] = p;
                    return r;
                }
                k++;
            } else {
                r[0] = -1;
                if (c > 0) {
                    c = f[c - 1];
                } else {
                    k++;
                }
            }
        }
        return new int[]{-1, -1, -1, -1};
    }

    // KMP algorithm going to the left
    public int[] left(char[] word, int[] f, int i, int j) {
        int c = 0;
        int k = i;
        int p = j;
        int[] r = {-1,-1,-1,-1};

        while (k >= 0) {
            if (word[c] == puzzle[k][p]) {
                if (r[0] == -1) {
                    r[0] = k;
                    r[1] = p;
                }
                c++;
                if (c == word.length) {
                    r[2] = k;
                    r[3] = p;
                    return r;
                }
                k--;
            } else {
                r[0] = -1;
                if (c > 0) {
                    c = f[c - 1];
                } else {
                    k--;
                }
            }
        }
        return new int[]{-1, -1, -1, -1};

    }

    // KMP algorithm going down
    public int[] down(char[] word, int[] f, int i, int j) {
        int c = 0;
        int k = i;
        int p = j;
        int[] r = {-1,-1,-1,-1};

        while (p < puzzle.length) {
            if (word[c] == puzzle[k][p]) {
                if (r[0] == -1) {
                    r[0] = k;
                    r[1] = p;
                }
                c++;
                if (c == word.length) {
                    r[2] = k;
                    r[3] = p;
                    return r;
                }
                p++;
            } else {
                r[0] = -1;
                if (c > 0) {
                    c = f[c - 1];
                } else {
                    p++;
                }
            }
        }
        return new int[]{-1, -1, -1, -1};

    }

    // KMP algorithm going up
    public int[] up(char[] word, int[] f, int i, int j) {
        int c = 0;
        int k = i;
        int p = j;
        int[] r = {-1,-1,-1,-1};

        while (p >= 0) {
            if (word[c] == puzzle[k][p]) {
                if (r[0] == -1) {
                    r[0] = k;
                    r[1] = p;
                }
                c++;
                if (c == word.length) {
                    r[2] = k;
                    r[3] = p;
                    return r;
                }
                p--;
            } else {
                r[0] = -1;
                if (c > 0) {
                    c = f[c - 1];
                } else {
                    p--;
                }
            }
        }
        return new int[]{-1, -1, -1, -1};

    }

    // KMP algorithm going right-up
    public int[] ru(char[] word, int[] f, int i, int j) {
        int c = 0;
        int k = i;
        int p = j;
        int[] r = {-1,-1,-1,-1};

        while (k < puzzle.length && p >= 0) {
            if (word[c] == puzzle[k][p]) {
                if (r[0] == -1) {
                    r[0] = k;
                    r[1] = p;
                }
                c++;
                if (c == word.length) {
                    r[2] = k;
                    r[3] = p;
                    return r;
                }
                p--;
                k++;
            } else {
                r[0] = -1;
                if (c > 0) {
                    c = f[c - 1];
                } else {
                    p--;
                    k++;
                }
            }
        }
        return new int[]{-1, -1, -1, -1};

    }

    // KMP algorithm going right-down
    public int[] rd(char[] word, int[] f, int i, int j) {
        int c = 0;
        int k = i;
        int p = j;
        int[] r = {-1,-1,-1,-1};

        while (p < puzzle.length && k < puzzle.length) {
            if (word[c] == puzzle[k][p]) {
                if (r[0] == -1) {
                    r[0] = k;
                    r[1] = p;
                }
                c++;
                if (c == word.length) {
                    r[2] = k;
                    r[3] = p;
                    return r;
                }
                p++;
                k++;
            } else {
                r[0] = -1;
                if (c > 0) {
                    c = f[c - 1];
                } else {
                    p++;
                    k++;
                }
            }
        }
        return new int[]{-1, -1, -1, -1};

    }

    // KMP algorithm going left-up
    public int[] lu(char[] word, int[] f, int i, int j) {
        int c = 0;
        int k = i;
        int p = j;
        int[] r = {-1,-1,-1,-1};

        while (p >= 0 && k >= 0) {
            if (word[c] == puzzle[k][p]) {
                if (r[0] == -1) {
                    r[0] = k;
                    r[1] = p;
                }
                c++;
                if (c == word.length) {
                    r[2] = k;
                    r[3] = p;
                    return r;
                }
                p--;
                k--;
            } else {
                r[0] = -1;
                if (c > 0) {
                    c = f[c - 1];
                } else {
                    p--;
                    k--;
                }
            }
        }
        return new int[]{-1, -1, -1, -1};

    }

    // KMP algorithm going left-down
    public int[] ld(char[] word, int[] f, int i, int j) {
        int c = 0;
        int k = i;
        int p = j;
        int[] r = {-1,-1,-1,-1};

        while (p < puzzle.length && k >= 0) {
            if (word[c] == puzzle[k][p]) {
                if (r[0] == -1) {
                    r[0] = k;
                    r[1] = p;
                }
                c++;
                if (c == word.length) {
                    r[2] = k;
                    r[3] = p;
                    return r;
                }
                p++;
                k--;
            } else {
                r[0] = -1;
                if (c > 0) {
                    c = f[c - 1];
                } else {
                    p++;
                    k--;
                }
            }
        }
        return new int[]{-1, -1, -1, -1};

    }
}