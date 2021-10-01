package net.lazyio.astral.ai.tasks;

import net.lazyio.astral.ai.ITask;
import net.lazyio.astral.ai.TaskResult;
import net.lazyio.astral.entity.Entity;

public class IdleTask implements ITask {

    private final float taskTime;
    private final Entity holder;

    private float currentTime;

    public IdleTask(Entity holder, float taskTime) {
        this.holder = holder;
        this.taskTime = taskTime;
    }

    @Override
    public TaskResult doTask(float dt) {
        if(this.currentTime < this.taskTime){
            this.currentTime += dt;
            this.holder.vel.x = 0f;
            return TaskResult.PASS;
        } else {
            this.currentTime = 0f;
        }
        return TaskResult.LOOP;
    }
}
