package BDD;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FournisseurExecutor {

    private static  ExecutorService executorService;

    public static ExecutorService creerExecutor(){
        if(executorService == null)
            executorService = Executors.newCachedThreadPool();
        return executorService;
    }
}
