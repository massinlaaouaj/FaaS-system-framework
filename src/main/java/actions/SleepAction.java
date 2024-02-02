package actions;

/**
 * Acción de dormir el hilo.
 */
public class SleepAction implements Action<Integer, String> {

    @Override
    public String run(Integer arg) {
        try{
            Thread.sleep(arg);
            return "Done!";
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return "sleepAction";
    }
}
