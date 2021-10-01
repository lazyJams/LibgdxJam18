package net.lazyio.astral.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import net.lazyio.astral.Astral;

public class EventManager {

    private final ObjectMap<Class<? extends Event>, ObjectSet<IEventListener>> bus = new ObjectMap<>();

    public static final String TAG = EventManager.class.getSimpleName();

    public void addListener(Class<? extends Event> eventClass, IEventListener listener) {
        ObjectSet<IEventListener> eventListeners = bus.get(eventClass);
        if (eventListeners == null) {
            eventListeners = new ObjectSet<>(8);
            bus.put(eventClass, eventListeners);
        }

        if(Astral.INST.debug) Gdx.app.log(TAG, "Adding new event listener to " + eventClass);
        eventListeners.add(listener);
    }

    public void sendEvent(Event event){
        if(Astral.INST.debug) Gdx.app.log(TAG, "Sending event listener to all listener on event: " + event.getClass().getSimpleName());
        ObjectSet<IEventListener> eventListeners = bus.get(event.getClass());
        if(eventListeners != null){
            eventListeners.forEach( eventListener -> eventListener.onEvent(event));
        }
    }
}
