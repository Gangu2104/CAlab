.data
a:
10
.text
main:
load %x0, $a, %x3
loop:
divi %x3, 10, %x4
muli %x4, 10, %x5
sub %x3, %x5, %x6
addi %x1, 1, %x1
store %x6, $a, %x1
divi %x3, 10, %x3
beq %x3, %x0, outloop
jmp loop
outloop:
pool:
beq %x11, %x1, palindrome
addi %x11, 1, %x11
load %x11, $a, %x12
addi %x1, 1, %x8
sub %x8, %x11, %x9
load %x9, $a, %x13
bne %x12, %x13, notpalindrome 
jmp pool
palindrome:
addi %x0, 1, %x10
end
notpalindrome:
subi %x0, 1, %x10
end