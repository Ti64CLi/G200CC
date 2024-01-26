package backend.riscv;

import backend.BackendOpcode;

public enum RISCVOpcode implements BackendOpcode {
    AUIPC,
    ADD,
    ADDI,
    AND,
    ANDI,
    BEQ,
    BGE,
    BGEU,
    BLT,
    BLTU,
    BNE,
    CALL,
    DIV,
    DIVU,
    ECALL,
    J,
    JAL,
    JALR,
    LB,
    LBU,
    LH,
    LHU,
    LUI,
    LW,
    MUL,
    MULH,
    MULHSU,
    MULHU,
    OR,
    ORI,
    REM,
    REMU,
    RET,
    SB,
    SH,
    SLL,
    SLLI,
    SLT,
    SLTU,
    SLTI,
    SLTIU,
    SRA,
    SRAI,
    SRL,
    SRLI,
    SUB,
    SW,
    XOR,
    XORI,
    ;

    @Override
    public String toString() {
        return switch (this) {
            case AUIPC -> "auipc";
            case ADD -> "add";
            case ADDI -> "addi";
            case AND -> "and";
            case ANDI -> "andi";
            case BEQ -> "beq";
            case BGE -> "bge";
            case BGEU -> "bgeu";
            case BLT -> "blt";
            case BLTU -> "bltu";
            case BNE -> "bne";
            case CALL -> "call";
            case DIV -> "div";
            case DIVU -> "divu";
            case ECALL -> "ecall";
            case J -> "j";
            case JAL -> "jal";
            case JALR -> "jalr";
            case LB -> "lb";
            case LBU -> "lbu";
            case LH -> "lh";
            case LHU -> "lhu";
            case LUI -> "lui";
            case LW -> "lw";
            case MUL -> "mul";
            case MULH -> "mulh";
            case MULHSU -> "mulhsu";
            case MULHU -> "mulhu";
            case OR -> "or";
            case ORI -> "ori";
            case REM -> "rem";
            case REMU -> "remu";
            case RET -> "ret";
            case SB -> "sb";
            case SH -> "sh";
            case SLL -> "sll";
            case SLLI -> "slli";
            case SLT -> "slt";
            case SLTU -> "sltu";
            case SLTI -> "slti";
            case SLTIU -> "sltiu";
            case SRA -> "sra";
            case SRAI -> "srai";
            case SRL -> "srl";
            case SRLI -> "srli";
            case SUB -> "sub";
            case SW -> "sw";
            case XOR -> "xor";
            case XORI -> "xori";
        };
    }
}
