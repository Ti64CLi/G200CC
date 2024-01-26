package backend.riscv;

public enum RISCVPhysicalRegister {
    ZERO, /* Hardwired zero */
    RA, /* Return address */
    SP, /* Stack pointer */
    GP, /* Global pointer */
    TP, /* Thread pointer */
    T0, /* Temporary register 0 */
    T1, /* Temporary register 1 */
    T2, /* Temporary register 2 */
    S0, /* Saved register 0 / Frame pointer */
    S1, /* Saved register 1 */
    A0, /* Function argument 0 / Return value 0 */
    A1, /* Function argument 1 / Return value 1 */
    A2, /* Function argument 2 */
    A3, /* Function argument 3 */
    A4, /* Function argument 4 */
    A5, /* Function argument 5 */
    A6, /* Function argument 6 */
    A7, /* Function argument 7 */
    S2, /* Saved register 2 */
    S3, /* Saved register 3 */
    S4, /* Saved register 4 */
    S5, /* Saved register 5 */
    S6, /* Saved register 6 */
    S7, /* Saved register 7 */
    S8, /* Saved register 8 */
    S9, /* Saved register 9 */
    S10, /* Saved register 10 */
    S11, /* Saved register 11 */
    T3, /* Temporary register 3 */
    T4, /* Temporary register 4 */
    T5, /* Temporary register 5 */
    T6; /* Temporary register 6 */

    @Override
    public String toString() {
        return switch (this) {
            case ZERO -> "zero";
            case RA -> "ra";
            case SP -> "sp";
            case GP -> "gp";
            case TP -> "tp";
            case T0 -> "t0";
            case T1 -> "t1";
            case T2 -> "t2";
            case S0 -> "s0";
            case S1 -> "s1";
            case A0 -> "a0";
            case A1 -> "a1";
            case A2 -> "a2";
            case A3 -> "a3";
            case A4 -> "a4";
            case A5 -> "a5";
            case A6 -> "a6";
            case A7 -> "a7";
            case S2 -> "s2";
            case S3 -> "s3";
            case S4 -> "s4";
            case S5 -> "s5";
            case S6 -> "s6";
            case S7 -> "s7";
            case S8 -> "s8";
            case S9 -> "s9";
            case S10 -> "s10";
            case S11 -> "s11";
            case T3 -> "t3";
            case T4 -> "t4";
            case T5 -> "t5";
            case T6 -> "t6";
        };
    }
    
    public static RISCVPhysicalRegister getArgRegister(int index) {
    	return switch (index) {
    		case 0 -> A0;
    		case 1 -> A1;
    		case 2 -> A2;
    		case 3 -> A3;
    		case 4 -> A4;
    		case 5 -> A5;
    		case 6 -> A6;
    		case 7 -> A7;
			default -> throw new IllegalArgumentException("Unexpected value: " + index);
    	};
    }
}
