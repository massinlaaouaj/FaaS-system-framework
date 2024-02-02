package actions;

import java.io.Serializable;
import java.util.Map;

/**
 * Acción de suma.
 */
public class AddAction implements Action<Map<String,Integer>, Integer>, Serializable {

    @Override
    public Integer run(Map<String, Integer> arg) {

        //Simulamos que una suma lleva 100 ms, para visualizar mejor el resultado
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}

        return arg.get("x") + arg.get("y");
    }

    @Override
    public String getName() {
        return "addAction";
    }
}
