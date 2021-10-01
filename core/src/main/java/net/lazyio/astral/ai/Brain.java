package net.lazyio.astral.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Queue;

public class Brain {

    private final int maxTasks = 8;
    private int currentTaskCount;
    private Queue<ITask> tasks = new Queue<>(this.maxTasks);
    private ITask currentTask = null;

    public void addTask(ITask task) {
        if (this.currentTaskCount < this.maxTasks) {
            this.tasks.addLast(task);
            this.currentTaskCount++;
        } else {
            System.out.println("You may need to change the max number of tasks.");
        }
    }

    public void removeTask() {
        this.tasks.removeFirst();
    }

    public void moveToTail(ITask task) {
        this.tasks.addLast(task);
        this.tasks.removeFirst();
    }

    public void update(float delta, boolean doRnd) {
        if (this.tasks.notEmpty()) {
            TaskResult taskResult = this.tasks.first().doTask(delta);
            switch (taskResult) {
                case SUCCESS:
                case FAIL:
                    this.removeTask();
                    break;
                case LOOP:
                    this.moveToTail(this.tasks.first());
                    break;
                case PASS:
                    break;
            }
        }
    }

    public void forceTaskResult(TaskResult taskResult){
        switch (taskResult) {
            case SUCCESS:
            case FAIL:
                this.removeTask();
                break;
            case LOOP:
                this.moveToTail(this.tasks.first());
                break;
            case PASS:
                break;
        }
    }
}
