.data
n:
10
.text
main:
load %x0, $n, %x29
addi %x0, 65535, %x1
sub %x1, %x29, %x24
store %x31, $n, %x1
addi %x31, 1, %x31
subi %x1, 1, %x1
store %x31, $n, %x1
subi %x1, 1, %x1
loop:
beq %x1, %x24, stop
add %x31, %x30, %x25
store %x25, $n, %x1
subi %x1, 1, %x1
addi %x31, 0, %x30
addi %x25, 0, %x31
jmp loop
stop:
end