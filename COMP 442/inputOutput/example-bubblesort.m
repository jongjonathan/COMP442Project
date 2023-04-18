           entry
           addi r14,r0,topaddr
           % processing: n := size
           lw r1,size(r0)
           sw n(r0),r1
           % processing: t1 := 0
           addi r1,r0,0
           sw t1(r0),r1
           % processing: i := t1
           lw r1,t1(r0)
           sw i(r0),r1
           % processing: t2 := 0
           addi r1,r0,0
           sw t2(r0),r1
           % processing: j := t2
           lw r1,t2(r0)
           sw j(r0),r1
           % processing: t3 := 0
           addi r1,r0,0
           sw t3(r0),r1
           % processing: temp := t3
           lw r1,t3(r0)
           sw temp(r0),r1
           % processing: n := size
           lw r1,size(r0)
           sw n(r0),r1
           % processing: t4 := 0
           addi r1,r0,0
           sw t4(r0),r1
           % processing: i := t4
           lw r1,t4(r0)
           sw i(r0),r1
           % processing: t5 := 0
           addi r1,r0,0
           sw t5(r0),r1
           % processing: t6 := 64
           addi r1,r0,64
           sw t6(r0),r1
           % processing: arr := t6
           lw r1,t6(r0)
           sw arr(r0),r1
           % processing: t7 := 1
           addi r1,r0,1
           sw t7(r0),r1
           % processing: t8 := 34
           addi r1,r0,34
           sw t8(r0),r1
           % processing: arr := t8
           lw r1,t8(r0)
           sw arr(r0),r1
           % processing: t9 := 2
           addi r1,r0,2
           sw t9(r0),r1
           % processing: t10 := 25
           addi r1,r0,25
           sw t10(r0),r1
           % processing: arr := t10
           lw r1,t10(r0)
           sw arr(r0),r1
           % processing: t11 := 3
           addi r1,r0,3
           sw t11(r0),r1
           % processing: t12 := 12
           addi r1,r0,12
           sw t12(r0),r1
           % processing: arr := t12
           lw r1,t12(r0)
           sw arr(r0),r1
           % processing: t13 := 4
           addi r1,r0,4
           sw t13(r0),r1
           % processing: t14 := 22
           addi r1,r0,22
           sw t14(r0),r1
           % processing: arr := t14
           lw r1,t14(r0)
           sw arr(r0),r1
           % processing: t15 := 5
           addi r1,r0,5
           sw t15(r0),r1
           % processing: t16 := 11
           addi r1,r0,11
           sw t16(r0),r1
           % processing: arr := t16
           lw r1,t16(r0)
           sw arr(r0),r1
           % processing: t17 := 6
           addi r1,r0,6
           sw t17(r0),r1
           % processing: t18 := 90
           addi r1,r0,90
           sw t18(r0),r1
           % processing: arr := t18
           lw r1,t18(r0)
           sw arr(r0),r1
           % processing: t19 := 7
           addi r1,r0,7
           sw t19(r0),r1
           % processing: t20 := 7
           addi r1,r0,7
           sw t20(r0),r1
           % processing: t21 := 7
           addi r1,r0,7
           sw t21(r0),r1
           hlt

           % space for variable n
n res 4
           % space for variable i
i res 4
           % space for variable j
j res 4
           % space for variable temp
temp res 4
           % space for constant 0
t1         res 4
           % space for constant 0
t2         res 4
           % space for constant 0
t3         res 4
           % space for variable n
n res 4
           % space for variable i
i res 4
           % space for constant 0
t4         res 4
           % space for variable arr
arr res 4
           % space for constant 0
t5         res 4
           % space for constant 64
t6         res 4
           % space for constant 1
t7         res 4
           % space for constant 34
t8         res 4
           % space for constant 2
t9         res 4
           % space for constant 25
t10        res 4
           % space for constant 3
t11        res 4
           % space for constant 12
t12        res 4
           % space for constant 4
t13        res 4
           % space for constant 22
t14        res 4
           % space for constant 5
t15        res 4
           % space for constant 11
t16        res 4
           % space for constant 6
t17        res 4
           % space for constant 90
t18        res 4
           % space for constant 7
t19        res 4
           % space for constant 7
t20        res 4
           % space for constant 7
t21        res 4
           % buffer space used for console output
buf        res 20

