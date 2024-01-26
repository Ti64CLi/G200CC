package backend.riscv;

public class RISCVRegister {
    private static int number = 0;
    private RISCVPhysicalRegister physicalRegister = null;
    private final int id;

    public RISCVRegister() {
        id = number++;
    }
    
    public RISCVRegister(RISCVPhysicalRegister pr) {
        id = number++;
        this.physicalRegister = pr;
    }

    public RISCVPhysicalRegister getPhysicalRegister() {
        return physicalRegister;
    }

    public void setPhysicalRegister(RISCVPhysicalRegister physicalRegister) {
        this.physicalRegister = physicalRegister;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        if(physicalRegister != null)
            return physicalRegister.toString();
        return "%" + id;
    }
}
