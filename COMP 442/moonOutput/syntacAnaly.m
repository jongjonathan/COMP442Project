           entry
           addi r14,r0,topaddr
           % processing: t1 := 0.0
           addi r1,r0,0.0
           sw t1(r0),r1
           % processing: result := t1
           lw r1,t1(r0)
           sw result(r0),r1
           % processing: t2 := a * x
           lw r2,a(r0)
           lw r3,x(r0)
           mul r1,r2,r3
           sw t2(r0),r1
           % processing: t3 := a + b
           lw r3,a(r0)
           lw r2,b(r0)
           add r1,r3,r2
           sw t3(r0),r1
           % processing: result := t2
           lw r1,t2(r0)
           sw result(r0),r1
           % processing: put(result)
           lw r1,result(r0)
           % put value on stack
           sw -8(r14),r1
           % link buffer to stack
           addi r1,r0, buf
           sw -12(r14),r1
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr
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
           % processing: j := t5
           lw r1,t5(r0)
           sw j(r0),r1
           % processing: t6 := 0
           addi r1,r0,0
           sw t6(r0),r1
           % processing: temp := t6
           lw r1,t6(r0)
           sw temp(r0),r1
           % processing: n := size
           lw r1,size(r0)
           sw n(r0),r1
           % processing: t7 := 0
           addi r1,r0,0
           sw t7(r0),r1
           % processing: i := t7
           lw r1,t7(r0)
           sw i(r0),r1
           % processing: t8 := one + two
           lw r2,one(r0)
           lw r3,two(r0)
           add r1,r2,r3
           sw t8(r0),r1
           % processing: result := t8
           lw r1,t8(r0)
           sw result(r0),r1
           % processing: t9 := one + two
           lw r3,one(r0)
           lw r2,two(r0)
           add r1,r3,r2
           sw t9(r0),r1
           % processing: result := t9
           lw r1,t9(r0)
           sw result(r0),r1
           % processing: put(result)
           lw r1,result(r0)
           % put value on stack
           sw -8(r14),r1
           % link buffer to stack
           addi r1,r0, buf
           sw -12(r14),r1
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr
           % processing: t10 := 0
           addi r1,r0,0
           sw t10(r0),r1
           % processing: t11 := 0
           addi r1,r0,0
           sw t11(r0),r1
           % processing: t12 := 64
           addi r1,r0,64
           sw t12(r0),r1
           % processing: arr := t12
           lw r1,t12(r0)
           sw arr(r0),r1
           % processing: t13 := 1
           addi r1,r0,1
           sw t13(r0),r1
           % processing: one := t13
           lw r1,t13(r0)
           sw one(r0),r1
           % processing: t14 := 2
           addi r1,r0,2
           sw t14(r0),r1
           % processing: two := t14
           lw r1,t14(r0)
           sw two(r0),r1
           % processing: t15 := 0
           addi r1,r0,0
           sw t15(r0),r1
           % processing: t16 := 0
           addi r1,r0,0
           sw t16(r0),r1
           % processing: one := 
           lw r1,(r0)
           sw one(r0),r1
           hlt

           % space for constant 0.0
t1         res 4
           % space for a * x
t2         res 4
           % space for a + b
t3         res 4
           % space for variable n
n res 4
           % space for variable j
j res 4
           % space for variable temp
temp res 4
           % space for constant 0
t4         res 4
           % space for constant 0
t5         res 4
           % space for constant 0
t6         res 4
           % space for variable n
n res 4
           % space for variable i
i res 4
           % space for constant 0
t7         res 4
           % space for variable result
result res 4
           % space for one + two
t8         res 4
           % space for variable result
result res 4
           % space for one + two
t9         res 4
           % space for variable arr
arr res 4
           % space for constant 0
t10        res 4
           % space for constant 0
t11        res 4
           % space for constant 64
t12        res 4
           % space for variable one
one res 4
           % space for variable two
two res 4
           % space for constant 1
t13        res 4
           % space for constant 2
t14        res 4
           % space for constant 0
t15        res 4
           % space for constant 0
t16        res 4
           % buffer space used for console output
buf        res 20

