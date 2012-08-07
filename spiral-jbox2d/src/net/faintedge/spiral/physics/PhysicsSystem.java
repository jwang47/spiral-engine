package net.faintedge.spiral.physics;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.faintedge.spiral.core.CoreSystem;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsSystem extends CoreSystem implements ContactListener {

  private float timeStep = 1.0f/60.0f;
  private float timeStepMillis = timeStep*1000.0f;
  private int velocityIterations = 6;
  private int positionIterations = 2;
  
  private float timeMultiplier = 5;
  private float timeAccumulator = 0;
  
  private World world;
  private HashMap<Long, PhysicsControl> controls;
  private Queue<PhysicsControl> controlsToAdd;
  private Queue<PhysicsControl> controlsToRemove;
  private Queue<Operation> preUpdateOperations;
  private PhysicsContactListener contactListener;
  
  public PhysicsSystem() {
    controls = new HashMap<Long, PhysicsControl>();
    controlsToAdd = new ConcurrentLinkedQueue<PhysicsControl>();
    controlsToRemove = new ConcurrentLinkedQueue<PhysicsControl>();
    preUpdateOperations = new ConcurrentLinkedQueue<Operation>();
    world = new World(new Vec2(0, 0), true);
    world.setContactListener(this);
    contactListener = null;
  }
  
  @Override
  public void update(int delta) {
    while (!controlsToAdd.isEmpty()) {
      doAdd(controlsToAdd.poll());
    }
    while (!controlsToRemove.isEmpty()) {
      doRemove(controlsToRemove.poll());
    }
    while (!preUpdateOperations.isEmpty()) {
      preUpdateOperations.poll().execute();
    }
    timeAccumulator += delta*timeMultiplier;
    while (timeAccumulator > timeStepMillis) {
      world.step(timeStep, velocityIterations, positionIterations);
      timeAccumulator -= timeStepMillis;
    }
  }
  
  public void enqueuePreUpdateOperation(Operation operation) {
    preUpdateOperations.add(operation);
  }

  public void add(PhysicsControl control) {
    controlsToAdd.add(control);
  }
  
  protected void doAdd(PhysicsControl control) {
    Body body = world.createBody(control.getBodyDef());
    body.createFixture(control.getFixtureDef());
    body.setUserData(control);
    
    control.setBody(body);
    controls.put(control.getId(), control);
  }

  public void remove(PhysicsControl control) {
    controlsToRemove.add(control);
  }
  
  protected void doRemove(PhysicsControl control) {
    world.destroyBody(control.getBody());
  }

  @Override
  public void beginContact(Contact contact) {
    if (contactListener != null) {
      Fixture fixtureA = contact.getFixtureA();
      Fixture fixtureB = contact.getFixtureB();
      
      if (fixtureA != null && fixtureB != null) {
        PhysicsControl controlA = (PhysicsControl) contact.getFixtureA().getBody().getUserData();
        PhysicsControl controlB = (PhysicsControl) contact.getFixtureB().getBody().getUserData();
        if (controlA != null && controlB != null) {
          contactListener.onContactBegin(controlA.getOwner(), controlB.getOwner());
        }
      }
    }
  }

  @Override
  public void endContact(Contact contact) {
    if (contactListener != null) {
      Fixture fixtureA = contact.getFixtureA();
      Fixture fixtureB = contact.getFixtureB();
      
      if (fixtureA != null && fixtureB != null) {
        PhysicsControl controlA = (PhysicsControl) contact.getFixtureA().getBody().getUserData();
        PhysicsControl controlB = (PhysicsControl) contact.getFixtureB().getBody().getUserData();
        if (controlA != null && controlB != null) {
          contactListener.onContactEnd(controlA.getOwner(), controlB.getOwner());
        }
      }
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {}

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {}

  public PhysicsContactListener getContactListener() {
    return contactListener;
  }

  public void setContactListener(PhysicsContactListener contactListener) {
    this.contactListener = contactListener;
  }

  public World getWorld() {
    return world;
  }
  
}
