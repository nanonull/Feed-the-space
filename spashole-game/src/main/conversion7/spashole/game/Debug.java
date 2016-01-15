package conversion7.spashole.game;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import conversion7.gdxg.core.utils.Utils;
import org.slf4j.Logger;

import java.util.UUID;

public class Debug {
    private static final Logger LOG = Utils.getLoggerForClass();

    public static void main(String[] args) {

//        BehaviorTree<Object> behaviorTree = new BehaviorTree<>();
//
//        Array<Task<Object>> tasks = new Array<>();
//        Selector<Object> enemyFoundSelector = new Selector<>();
//        tasks.add(enemyFoundSelector);
//
//        new LeafTask<>()
//        enemyFoundSelector.addChild();
//
//
//        behaviorTree.addChild(new BranchTask<Object>(tasks));
//        behaviorTree.run();
    }

    public static class IsEnemyVisibleTask extends LeafTask<UUID> {

        @Override
        public void run(UUID entity) {
        }

        @Override
        protected Task<UUID> copyTo(Task<UUID> task) {
            return null;
        }
    }
}
