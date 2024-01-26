package backend.riscv.operand;

public class RISCVImmediateOperand extends RISCVOperand {
    private final Integer value;

    public RISCVImmediateOperand(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
    
    public String toString() {
    	return value.toString();
    }
}
