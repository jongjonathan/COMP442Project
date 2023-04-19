           entry
           addi r14,r0,topaddr
           % processing: t1 := 0
           addi r1,r0,0
           sw t1(r0),r1
           % processing: t2 := 64
           addi r1,r0,64
           sw t2(r0),r1
           % processing: arr := t2
           lw r1,t2(r0)
           sw arr(r0),r1
           % processing: t3 := 1
           addi r1,r0,1
           sw t3(r0),r1
           % processing: t4 := 34
           addi r1,r0,34
           sw t4(r0),r1
           % processing: arr := t4
           lw r1,t4(r0)
           sw arr(r0),r1
           % processing: t5 := 2
           addi r1,r0,2
           sw t5(r0),r1
           % processing: t6 := 25
           addi r1,r0,25
           sw t6(r0),r1
           % processing: arr := t6
           lw r1,t6(r0)
           sw arr(r0),r1
           % processing: t7 := 3
           addi r1,r0,3
           sw t7(r0),r1
           % processing: t8 := 12
           addi r1,r0,12
           sw t8(r0),r1
           % processing: arr := t8
           lw r1,t8(r0)
           sw arr(r0),r1
           % processing: t9 := 4
           addi r1,r0,4
           sw t9(r0),r1
           % processing: t10 := 22
           addi r1,r0,22
           sw t10(r0),r1
           % processing: arr := t10
           lw r1,t10(r0)
           sw arr(r0),r1
           % processing: t11 := 5
           addi r1,r0,5
           sw t11(r0),r1
           % processing: t12 := 11
           addi r1,r0,11
           sw t12(r0),r1
           % processing: arr := t12
           lw r1,t12(r0)
           sw arr(r0),r1
           % processing: t13 := 6
           addi r1,r0,6
           sw t13(r0),r1
           % processing: t14 := 90
           addi r1,r0,90
           sw t14(r0),r1
           % processing: arr := t14
           lw r1,t14(r0)
           sw arr(r0),r1
           hlt

           % space for variable n
n res 4
           % space for variable extraExtra
extraExtra res 4
           % space for variable i
i res 4
           % space for variable j
j res 4
           % space for variable arr
arr res 4
           % space for constant 0
t1         res 4
           % space for constant 64
t2         res 4
           % space for constant 1
t3         res 4
           % space for constant 34
t4         res 4
           % space for constant 2
t5         res 4
           % space for constant 25
t6         res 4
           % space for constant 3
t7         res 4
           % space for constant 12
t8         res 4
           % space for constant 4
t9         res 4
           % space for constant 22
t10        res 4
           % space for constant 5
t11        res 4
           % space for constant 11
t12        res 4
           % space for constant 6
t13        res 4
           % space for constant 90
t14        res 4
           % buffer space used for console output
buf        res 20

