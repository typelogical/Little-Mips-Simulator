

;Introduction:
;--------------------------------------------------------------------------------------------------------
;Three assembly lagnague programs.
;Program 1:
;Read a positive number n from the keyboard and compute and
;display its partial sum calculated by n(n+1)/2
;Program 2:
;Read a positive number from the keyboard and compute and display the ;smallest power of two, which is larger than the read number. E.g. for ;1000, the program should produce 1024.
;Program 3:
;Read a series of numbers, and determine and display the smallest number. ;Te first number read indicates how many numbers should be processed.


;Program 1:
;--------------------------------------------------------------------------------------------------------
; read from keyboard and place in location 11
; read 15
500b
; lw r0 b
700b
; load 1 stored in memory location 12 into reg 1
; lw r1 A
711c
; load 2 stored in memory location 13 into reg 2
; lw r2 B
720d
; add n and 1 and place in r1
; add r3 r0 r1
1123
; mult r0 and r3 and place in r4
; mult r4 r0 r3
3403
; div r4 by 2 and place in r4
; div r4 r2 r4
4424
; sw r4 14
840e
; output r4
600e
; A
; B
; halt
c000
0001
0002


;Program 2:
;--------------------------------------------------------------------------------------------------------
; read pos num and place in r0
; read 15
500b
; ldw r0 15
700c
; load 1 in memoery location A into r1
ldw r1 16
710d
; load 2 in memoery location B into r2
; ldw r2 18
720e
; while r1 less than r0
	; mult r1 and r2 place in r2
;mult r2 r1 r2
3212
;sub r3 r0 r2
2302
;bng r3 8
b308
;j 5
9005
; sw r1 C
810f
; ouput r1
; wri r1
600f
; halt
c000
0000
0005
0006


;Programm 3:
;--------------------------------------------------------------------------------------------------------
; read num and place in r0
; read 15
; ldw r0 15
5012
7012
; load 1 in memory location 16 into r1
ldw r1 16
7113
; while r0 greater than 0
	; sub 1 from r0 and place in r0
	; sub r0 r0 r1
	2001
	; bzr r0 B
	a013	
	; read num and place in r2
	; read 17
	5014
	; ldw r2 17
	7214
	; if r1 greater than r2, place r2 in r3
	; sub r4 r2 r3
	2423
	; bng r4 11
	b4015
	; j 4
	9004
	; add r5 r3 r5		; addr 11
	1535
	; sub r3 r3 r5
	2335
	; add r3 r3 r2
	1332
	; j 4
	9004
; output r1
; sw r1 F
8116
;wri F
6016
;halt
c000
0000
0001
0000
0000