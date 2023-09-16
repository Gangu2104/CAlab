.data
a:
10
20
30
40
50
60
70
80
n:
8
.text
main:
load %x0, $n, %x1
loop:
load %x0, $a, %x2
load %x20, $a, %x22
bgt %x22, %x2, count
addi %x20, 1, %x20
beq %x1, %x20, exit
jmp loop
count:
store %x22, $a, %x0
store %x2, $a, %x20
addi %x20, 1, %x20
bgt %x1, %x20, loop
addi %x0, 1, %x0
sub %x20, %x20, %x20
add %x20, %x0, %x20
bgt %x1, %x0, loop
end
exit:
addi %x0, 1, %x0
sub %x20, %x20, %x20
add %x20, %x0, %x20
bgt %x1, %x0, loop
end