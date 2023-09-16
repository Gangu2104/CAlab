.data
a:
103
.text
main:
addi %x0, 2, %x3
load %x0, $a, %x4
loop:
beq %x4, %x3, endLoop
div %x4, %x3, %x5
mul %x5, %x3, %x6
beq %x4, %x6, notPrime
addi %x3, 1, %x3
jmp loop
endLoop:
addi %x0, 1, %x10
end
notPrime:
subi %x0, 1, %x10
end