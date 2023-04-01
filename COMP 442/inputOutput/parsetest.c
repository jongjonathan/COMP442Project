           entry
           addi r14,r0,topaddr
           hlt

           % space for variable n
n res 4
           % space for variable i
i res 4
           % space for variable j
j res 4
           % buffer space used for console output
buf        res 20

