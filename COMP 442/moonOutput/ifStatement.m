           entry
           addi r14,r0,topaddr
           % processing: t1 := 4
           addi r1,r0,4
           sw t1(r0),r1
           % processing: p := t1
           lw r1,t1(r0)
           sw p(r0),r1
           % processing: t2 := 5
           addi r1,r0,5
           sw t2(r0),r1
           % processing: n := t2
           lw r1,t2(r0)
           sw n(r0),r1
           % processing: put(p)
           lw r1,p(r0)
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
           % processing: put(n)
           lw r1,n(r0)
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

           % space for variable p
p res 4
           % space for variable n
n res 4
           % space for constant 4
t1         res 4
           % space for constant 5
t2         res 4
           % buffer space used for console output
buf        res 20

