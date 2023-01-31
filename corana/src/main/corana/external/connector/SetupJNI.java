package main.corana.external.connector;

import main.corana.emulator.semantics.Environment;
import main.corana.emulator.semantics.Memory;
import main.corana.enums.Variation;
import main.corana.executor.BinParser;
import main.corana.executor.Corana;
import main.corana.executor.Executor;
import main.corana.pojos.BitVec;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupJNI {

    public Map.Entry<Environment, List<String>> execJNI(File libF, String methodName, Environment initEnv) {
        //Logs.initLog("./results/" + name + ".log");
        Corana.inpFile = libF.getName();
        HashMap<String, Map.Entry> methodInfo = BinParser.getJNIFunctions(libF.getAbsolutePath());
        Map.Entry<Long, Long> startEndPair = methodInfo.get(methodName);
        long start = startEndPair.getKey();
        long end = startEndPair.getValue()-8;
        return Executor.customExecute(Variation.M0, libF.getPath(), start, end, initEnv);
        //Logs.endLog();
    }

    public static void main(String[] args) {
        File dir = new File("/home/va/eclipse-workspace/JNIExample/src/libhello.so");
        SetupJNI su = new SetupJNI();
        Environment initEnv = new Environment();
        initEnv.register.set('0', ArithmeticUtils.IntegerToBitVec(10));
        initEnv.register.set('1', ArithmeticUtils.IntegerToBitVec(231));
        initEnv.register.set('2', new BitVec("a_SYM_1"));
        initEnv.register.set('3', new BitVec("b_SYM_2"));
        initEnv.stacks.push(new BitVec("c_SYM_3"));
        //initEnv.register.set('2', new BitVec("y_SYM"));
        Map.Entry<Environment, List<String>> result = su.execJNI(dir, "calPerimeter", initEnv);

        System.out.println(result.getKey().register.toString());
        System.out.println(result.getValue());

        //String smtLibContent = "(= (bvslt (bvsub y_SYM #x00000000) #x00000000) (or (bvsgt (bvsub y_SYM #x00000000) #x7fffffff) (bvslt (bvsub y_SYM #x00000000) #x80000001)))";


    }
}
