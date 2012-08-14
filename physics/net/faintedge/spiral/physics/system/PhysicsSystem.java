package net.faintedge.spiral.physics.system;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.physics.Physics;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import util.Log;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;

public class PhysicsSystem extends IntervalEntitySystem implements ContactListener {

  private static float timeStep = 1.0f / 60.0f;
  private static int timeStepMillis = (int) (timeStep * 1000.0f);
  
  private ComponentMapper<Transform> transformMapper;
  private ComponentMapper<Physics> physicsMapper;
  
  private int velocityIterations = 6;
  private int positionIterations = 2;
  
  private World world;
  private Vec2 gravity;
  private float ptm;
  
  private Queue<Entity> toAdd = new ConcurrentLinkedQueue<Entity>();
  private Queue<Entity> toRemove = new ConcurrentLinkedQueue<Entity>();
  
  public PhysicsSystem(Vec2 gravity, float ptm) {
    super(timeStepMillis, Transform.class, Physics.class);
    this.gravity = gravity;
    this.ptm = ptm;
  }

  @Override
  protected void initialize() {
    transformMapper = new ComponentMapper<Transform>(Transform.class, super.world);
    physicsMapper = new ComponentMapper<Physics>(Physics.class, super.world);
    world = new World(gravity, true);
    world.setContactListener(this);
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
    while (!toAdd.isEmpty()) { doAdd(toAdd.poll()); }
    while (!toRemove.isEmpty()) { doRemove(toRemove.poll()); }
    world.step(timeStep, velocityIterations, positionIterations);
    
    // update entity positions
    for (int i = 0, s = entities.size(); i < s; i++) {
      Entity e = entities.get(i);
      Transform transform = transformMapper.get(e);
      Physics physics = physicsMapper.get(e);
      
      Vec2 position = physics.getBody().getPosition();
      transform.getTranslation().set(position.x * ptm, position.y * ptm);
      transform.setRotation((float) (physics.getBody().getAngle() * 180 / Math.PI));
    }
  }
  
  @Override
  protected void added(Entity e) {
    toAdd.add(e);
  }

  @Override
  protected void removed(Entity e) {
    toRemove.add(e);
  }

  protected void doAdd(Entity e) {
    Transform transform = transformMapper.get(e);
    Physics physics = physicsMapper.get(e);

    physics.getBodyDef().position.x = transform.getTranslation().getX() / ptm;
    physics.getBodyDef().position.y = transform.getTranslation().getY() / ptm;
    
    Body body = world.createBody(physics.getBodyDef());
    body.createFixture(physics.getFixtureDef());
    body.setUserData(physics);
    physics.setBody(body);
  }
  
  protected void doRemove(Entity e) {
    Physics physics = physicsMapper.get(e);
    world.destroyBody(physics.getBody());
  }

  public World getWorld() {
    return world;
  }

  @Override
  public void beginContact(Contact contact) {
    Body bodyA = contact.getFixtureA().getBody();
    Body bodyB = contact.getFixtureB().getBody();
    Object userDataA = bodyA.getUserData();
    Object userDataB = bodyB.getUserData();
    if (userDataA instanceof Physics && userDataB instanceof Physics) {
      Physics physicsA = (Physics) userDataA;
      Physics physicsB = (Physics) userDataB;
      physicsA.addCollision(physicsB, contact.getManifold());
      physicsB.addCollision(physicsA, contact.getManifold());
    }
  }

  @Override
  public void endContact(Contact contact) {
    Body bodyA = contact.getFixtureA().getBody();
    Body bodyB = contact.getFixtureB().getBody();
    Object userDataA = bodyA.getUserData();
    Object userDataB = bodyB.getUserData();
    if (userDataA instanceof Physics && userDataB instanceof Physics) {
      Physics physicsA = (Physics) userDataA;
      Physics physicsB = (Physics) userDataB;
      physicsA.removeCollision(physicsB);
      physicsB.removeCollision(physicsA);
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {
    // TODO Auto-generated method stub
    
  }

}
