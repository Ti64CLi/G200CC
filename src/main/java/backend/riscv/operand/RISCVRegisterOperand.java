package backend.riscv.operand;

import backend.riscv.RISCVPhysicalRegister;
import backend.riscv.RISCVRegister;

public class RISCVRegisterOperand extends RISCVOperand {
    public RISCVRegister register;

    public RISCVRegisterOperand() {
    	register = null;
    }
    
    public RISCVRegisterOperand(RISCVRegister r) {
        register = r; 
    }

    public RISCVRegisterOperand(RISCVPhysicalRegister physicalRegister) {
        this.register = new RISCVRegister();
        this.register.setPhysicalRegister(physicalRegister);
    }

    public String toString() {
    	if (register == null)
    		return "nullRegOperand";
		return register.toString();
    }
}
