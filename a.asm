Read a positive number n from the keyboard and compute and
display its partial sum calculated by n(n+1)/2


; read from keyboard and place in r0
read 15
lw r0 15
; load 1 stored in memory location 16 into reg 1
lw r1 16
; load 2 stored in memory location 17 into reg 2
lw r2 17
; add n and 1 and place in r1
add r3 r0 r1
; mult r0 and r3 and place in r2
mult r4 r0 r3
; div r4 by 2 and place in r4
div r4 r2 r4
; output r4


Read a positive number from the keyboard and compute and display the smallest power of two, which is larger than the read number. E.g. for 1000, the program should produce 1024.

; read pos num and place in r0
read 15
ldw r0 15

; load 1 in memoery location 16 into r1
ldw r1 16
; load 2 in memoery location 17 into r2
ldw r2 18
; while r1 less than r0
	; mult r1 and r2 place in r2
mult r2 r1 r2
sub r3 r0 r2
bzr r3 8
j 5
; ouput r1
wri r1
halt

Read a series of numbers, and determine and display the smallest number. Te first number read indicates how many numbers should be processed.

; read num and place in r0
read 15
ldw r0 15
; load 1 in memory location 16 into r1
ldw r1 16
; while r0 greater than 0
	; sub 1 from r0 and place in r0
	sub r0 r0 r1
	bzr r0 15
	; read num and place in r2
	read 17
	ldw r2 17
	; if r1 greater than r2, place r2 in r3
	sub r4 r2 r3
	bng r4 11
	j 4
	add r3 r5		; addr 11
	sub r3 r3 r3
	add r3 r3 r2
	j 4
; output r1
wri r3
halt

