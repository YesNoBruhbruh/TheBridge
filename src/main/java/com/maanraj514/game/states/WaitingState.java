package com.maanraj514.game.states;

import com.maanraj514.BridgePlugin;
import com.maanraj514.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WaitingState extends GameState{

    private BukkitTask task;

    @Override
    public void onEnable(BridgePlugin plugin){
        List<String> jokes = new ArrayList<>();
        jokes.add(MessageUtil.rainbow("What do you call a fake noodle? An impasta."));
        jokes.add(MessageUtil.rainbow("Why did the scarecrow win an award? Because he was outstanding in his field."));
        jokes.add(MessageUtil.rainbow("What do you call a cow that plays an instrument? A moosician."));
        jokes.add(MessageUtil.rainbow("Why did the chicken cross the road? To get to the other side."));
        jokes.add(MessageUtil.rainbow("Why did the tomato turn red? Because it saw the salad dressing."));
        jokes.add(MessageUtil.rainbow("Why did the cookie go to the doctor? Because it was feeling crumbly."));
        jokes.add(MessageUtil.rainbow("Why did the banana go to the doctor? Because it wasn't peeling well."));
        jokes.add(MessageUtil.rainbow("Why did the math book look so sad? Because it had too many problems."));
        jokes.add(MessageUtil.rainbow("Why did the bicycle fall over? Because it was two-tired."));
        jokes.add(MessageUtil.rainbow("Why did the coffee file a police report? It got mugged."));
        jokes.add(MessageUtil.rainbow("What do you call a bear with no teeth? A gummy bear."));
        jokes.add(MessageUtil.rainbow("Why did the chicken cross the playground? To get to the other slide."));
        jokes.add(MessageUtil.rainbow("Why don't scientists trust atoms? Because they make up everything."));
        jokes.add(MessageUtil.rainbow("Never criticize someone until you have walked a mile in their shoes. That way, when you criticize them, you’ll be a mile away, and you’ll have their shoes."));
        jokes.add(MessageUtil.rainbow("I couldn’t figure out why the baseball kept getting larger. Then it hit me."));
        jokes.add(MessageUtil.rainbow("What do you call security guards working outside Samsung shops? Guardians of the Galaxy."));

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Random random = ThreadLocalRandom.current();
            String randomJoke = jokes.get(random.nextInt(jokes.size()));

            game.broadcast(randomJoke);
        }, 0L, 20*30L);

    }

    @Override
    public void onDisable(){
        if (task != null){
            task.cancel();
            task = null;
        }
    }
}