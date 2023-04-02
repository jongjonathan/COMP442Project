           entry
           addi r14,r0,topaddr
           % processing: t1 := 1 + 2
           lw r2,1(r0)
           lw r3,2(r0)
           add r1,r2,r3
           sw t1(r0),r1
           % processing: k := t1
           lw r1,t1(r0)
           sw k(r0),r1
           % processing: t2 := 3 * k
           lw r3,3(r0)
           lw r2,k(r0)
           mul r1,r3,r2
           sw t2(r0),r1
           % processing: n := t2
           lw r1,t2(r0)
           sw n(r0),r1
           hlt

           % space for variable n
n res 4
           % space for variable k
k res 4
           % space for 1 + 2
t1         res 4
           % space for 3 * k
t2         res 4
           % buffer space used for console output
buf        res 20

