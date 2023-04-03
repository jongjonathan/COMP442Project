           entry
           addi r14,r0,topaddr
           % processing: t1 := 0
           addi r1,r0,0
           sw t1(r0),r1
           % processing: t2 := 6
           addi r1,r0,6
           sw t2(r0),r1
           % processing: arr := t2
           lw r1,t2(r0)
           sw arr(r0),r1
           % processing: t3 := 5
           addi r1,r0,5
           sw t3(r0),r1
           % processing: k := t3
           lw r1,t3(r0)
           sw k(r0),r1
           % processing: t4 := 7
           addi r1,r0,7
           sw t4(r0),r1
           % processing: n := t4
           lw r1,t4(r0)
           sw n(r0),r1
           % processing: put(k)
           lw r1,k(r0)
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
           % processing: put(i)
           lw r1,i(r0)
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
           hlt

           % space for variable arr
arr res 4
           % space for variable n
n res 4
           % space for variable k
k res 4
           % space for variable i
i res 4
           % space for variable d
d res 4
           % space for constant 0
t1         res 4
           % space for constant 6
t2         res 4
           % space for constant 5
t3         res 4
           % space for constant 7
t4         res 4
           % buffer space used for console output
buf        res 20

