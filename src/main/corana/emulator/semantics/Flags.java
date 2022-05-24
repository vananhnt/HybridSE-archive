package main.corana.emulator.semantics;


import main.corana.pojos.BitBool;
import main.corana.utils.MyStr;

public class Flags {
    public BitBool N, Z, C, V, Q, GE;

    public Flags(boolean n, boolean z, boolean c, boolean v, boolean q, boolean ge) {
        N = new BitBool("n_SYM", n);
        Z = new BitBool("z_SYM", z);
        C = new BitBool("c_SYM", c);
        V = new BitBool("v_SYM", v);
        Q = new BitBool("q_SYM", q);
        GE = new BitBool("ge_SYM", ge);
    }

    @Override
    public String toString() {
        return new MyStr("+ Flags:",
                "\n\t- N:", N.isConcreteValue() ? 1 : 0, "\t", N.getSym(),
                "\n\t- Z:", Z.isConcreteValue() ? 1 : 0, "\t", Z.getSym(),
                "\n\t- C:", C.isConcreteValue() ? 1 : 0, "\t", C.getSym(),
                "\n\t- V:", V.isConcreteValue() ? 1 : 0, "\t", V.getSym(),
                "\n\t- Q:", Q.isConcreteValue() ? 1 : 0, "\t", Q.getSym(),
                "\n\t- GE:", GE.isConcreteValue() ? 1 : 0, "\t", GE.getSym()).value();
    }
}