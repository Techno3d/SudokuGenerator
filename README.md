# A sudoku generator made in Java

Took me so long


## How it works

Each 3x3 has the numbers 1 through 9 placed in it, like a sudoku board
The algorithim is basically sorting the 2d array twice

### Horizontal sorting

Sorts each row except for the 3rd, 6th and 9th rows (indexes 2,5,8).
If a number was already seen in that row, it will try to find another number in the same box (3x3) as the offender, and swap the two.
If there are no numbers it can swap with, we back track and find the previous number that is equal to the current offender, and makes that number the new offender. We then do the swap process with that number.

We can safely ignore the 3rd, 6th, and 9th rows because there will be no unsorted numbers for any number in each box that is not on the same row as what we are sorting.
In fact, that row will be sorted if the 2 rows on top of it are sorted.

### Vertical sorting

Vertical sorting sorts each collumn except the 3rd, 6th, and 9th collumns.
Because each row is sorted already, when we find an offending number, we only swap with numbers in the same box and same row.
This allows us to keep the rows sorted while sorting the collumns. If we can't swap any number with an offender, we have to backtrack to the previous number that is equal to the current offender.
If there is no previous number for us to swap with, then we do a "stupid swap" with the number next to the offender. We find the other instance of the number we just swapped the offender with, and do the swap algorithim on it.
Eventually this will sort the entire board fully.

### Perculiarities
Strings are immutable, so I made a Cache class which is just a wrapper around String that can be modified from a different function.
The stupid swap might swap with the adjacent adjacent box because randomly for the 1st, 4th, and 7th collumns to avoid a recursive loop that leads to a stack overflow.


## This is for my APcs class project

Don't expect genius code from me.
Also I didn't have access to using arraylists, which is why I used Strings for that.
Also not the fasted algorithim in the world, but this code could be optimized and make it even faster, speaking of which

## Speed

On my computer, for 1,000,001 sudoku boards, it took about 36.854s, which is about 36.85396 micro seconds per board.
Suprisingly not that slow.
