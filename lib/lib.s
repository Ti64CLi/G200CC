.globl printChar
.globl exit
.globl _start

.section .text
_start:
    jal ra, main
    j exit

printChar:
    addi sp, sp, -16
    sw x17, 0(sp)
    sw x10, 4(sp)
    sw x11, 8(sp)
    sw x12, 12(sp)
    addi x17, x0, 64
    addi x10, x0, 0
    addi x11, sp, 4
    addi x12, x0, 1
    ecall
    lw x12, 12(sp)
    lw x11, 8(sp)
    lw x10, 4(sp)
    lw x17, 0(sp)
    addi sp, sp, 16
    jr ra
    
exit:
    addi x17, x0, 93
    addi x17, x0, 93
    ecall
