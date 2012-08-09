package net.faintedge.spiral.networked;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

public class SyncSystem<T extends Component, H extends SyncObject<T>> extends EntitySystem {

  private ComponentMapper<H> syncObjectMapper;
  private ComponentMapper<T> objectMapper;
  
  private SyncManager<T> manager;
  private Class<T> type;
  private Class<H> type2;

  public SyncSystem(Class<T> type, Class<H> type2, SyncManager<T> manager) {
    super(type, type2);
    this.type = type;
    this.type2 = type2;
    this.manager = manager;
  }

  @Override
  protected void initialize() {
    syncObjectMapper = new ComponentMapper<H>(type2, world);
    objectMapper = new ComponentMapper<T>(type, world);
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
    manager.processEntities(entities, world.getDelta());
  }

  @Override
  protected void added(Entity e) {
    manager.added(objectMapper.get(e), syncObjectMapper.get(e));
  }

  @Override
  protected void removed(Entity e) {
    manager.removed(objectMapper.get(e), syncObjectMapper.get(e));
  }

}
