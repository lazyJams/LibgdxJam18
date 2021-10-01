package net.lazyio.astral.util;

import net.lazyio.astral.util.function.BFunc;

import java.util.ArrayList;
import java.util.List;

public class TaskCompletion {

    public List<BFunc> actions;
    public int done = 0;

    public boolean isDone = false;

    public TaskCompletion(List<BFunc> actions) {
        this.actions = actions;
    }

    // FIXME: 01/10/2021 
    
    public void update(){
        if(!isDone){
            for (int i = actions.size() - 1; i > 0; i--) {
                if(actions.get(i).apply()){
                    done++;
                }
            }
        }

        if(done == actions.size()) isDone = true;
    }
}
