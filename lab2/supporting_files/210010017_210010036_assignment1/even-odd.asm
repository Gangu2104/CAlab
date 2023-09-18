.data
a:
10
.text
main:
load %x0, $a, %x6
divi %x6, 2, %x7
muli %x7, 2, %x8
beq %x6, %x8, even
addi %x0, 1, %x10
end
even:
subi %x0, 1, %x10
end