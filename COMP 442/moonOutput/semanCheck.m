           entry
           addi r14,r0,topaddr
           % processing: t1 := 5
           addi r1,r0,5
           sw t1(r0),r1
           % processing: p := 
           lw r1,(r0)
           sw p(r0),r1
           % processing: i := 
           lw r1,(r0)
           sw i(r0),r1
           % processing: n := size
           lw r1,size(r0)
           sw n(r0),r1
           % processing: t2 := 0
           addi r1,r0,0
           sw t2(r0),r1
           % processing: i := t2
           lw r1,t2(r0)
           sw i(r0),r1
           hlt

           % space for variable n
n res 4
           % space for variable i
i res 4
           % space for variable p
p res 4
           % space for variable size
size res 4
           % space for constant 5
t1         res 4
           % space for constant 0
t2         res 4
           % buffer space used for console output
buf        res 20

