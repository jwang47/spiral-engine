package net.faintedge.spiral.networked;


import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.core.Vector2;
import net.faintedge.spiral.physics.PhysicsControl;
import net.faintedge.spiral.physics.PhysicsSystem;
import net.faintedge.spiral.physics.control.PhysicsMoveControl;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;


public class PhysicsSpatialSyncCreate extends SynchronizeCreateMessage {

  private int requestId = -1;
  private boolean clientControlled = false;
  private SpatialSyncCreate spatial;
  
  // body stuff
  private BodyType bodyType;
  // for circle shape
  private float radius = 1;
  // for polygon shape
  private float[] centroid = null;
  private float[][] vertices = null;
  private float[][] normals = null;
  
  // fixture stuff
  private ShapeType shapeType;
  private float linearDamping = 0f;
  // other
  private float moveSpeed = -1;
  
  public int getRequestId() {
    return requestId;
  }
  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }
  public SpatialSyncCreate getSpatial() {
    return spatial;
  }
  public void setSpatial(SpatialSyncCreate spatial) {
    this.spatial = spatial;
  }
  public float getMoveSpeed() {
    return moveSpeed;
  }
  public void setMoveSpeed(float moveSpeed) {
    this.moveSpeed = moveSpeed;
  }
  public BodyType getBodyType() {
    return bodyType;
  }
  public void setBodyType(BodyType bodyType) {
    this.bodyType = bodyType;
  }
  public ShapeType getShapeType() {
    return shapeType;
  }
  public void setShapeType(ShapeType shapeType) {
    this.shapeType = shapeType;
  }
  public float getRadius() {
    return radius;
  }
  public void setRadius(float radius) {
    this.radius = radius;
  }
  public float[] getCentroid() {
    return centroid;
  }
  public void setCentroid(Vector2 centroid) {
    this.centroid = new float[] {centroid.getX(), centroid.getY()};
  }
  public float[][] getVertices() {
    return vertices;
  }
  public void setVertices(Vector2[] vertices) {
    this.vertices = new float[vertices.length][2];
    for(int i = 0; i < vertices.length; i++) {
      this.vertices[i][0] = vertices[i].getX();
      this.vertices[i][1] = vertices[i].getY();
    }
  }
  public float[][] getNormals() {
    return normals;
  }
  public void setNormals(Vector2[] normals) {
    this.normals = new float[normals.length][2];
    for(int i = 0; i < normals.length; i++) {
      this.normals[i][0] = normals[i].getX();
      this.normals[i][1] = normals[i].getY();
    }
  }
  public float getLinearDamping() {
    return linearDamping;
  }
  public void setLinearDamping(float linearDamping) {
    this.linearDamping = linearDamping;
  }
  
  public boolean isClientControlled() {
    return clientControlled;
  }
  public void setClientControlled(boolean clientControlled) {
    this.clientControlled = clientControlled;
  }
  /**
   * @param create
   * @param makeMoveControl
   * @param system
   * @param ptm Pixels to meters ratio for box2d physics
   * @return
   */
  public static Spatial createPhysicsSpatial(PhysicsSpatialSyncCreate create, boolean makeMoveControl, PhysicsSystem system, float ptm) {
    Spatial spatial = SpatialSyncCreate.createSpatial(create.getSpatial());
    
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = create.getBodyType();
    bodyDef.linearDamping = create.getLinearDamping();
    FixtureDef fixtureDef = new FixtureDef();
    if (create.getShapeType() == ShapeType.CIRCLE) {
      CircleShape circle = new CircleShape();
      circle.m_radius = create.getRadius();
      fixtureDef.shape = circle;
    } else if (create.getShapeType() == ShapeType.POLYGON) {
      PolygonShape polygon = new PolygonShape();
      int numVertices = create.vertices.length;
      polygon.m_centroid.set(create.centroid[0], create.centroid[1]);
      for (int i = 0; i < numVertices; i++) {
        polygon.m_vertices[i].set(create.vertices[i][0], create.vertices[i][1]);
        polygon.m_normals[i].set(create.normals[i][0], create.normals[i][1]);
      }
      polygon.m_vertexCount = numVertices;
      fixtureDef.shape = polygon;
    }
    
    PhysicsControl physics = new PhysicsControl(system, bodyDef, fixtureDef, ptm);
    spatial.addControl(physics);
    if (makeMoveControl && create.getMoveSpeed() >= 0) {
      spatial.addControl(new PhysicsMoveControl(physics, create.getMoveSpeed()));
    }
    return spatial;
  }
  
  public static PhysicsSpatialSyncCreate fromSpatial(Spatial spatial) {
    PhysicsSpatialSyncCreate msg = new PhysicsSpatialSyncCreate();
    msg.spatial = SpatialSyncCreate.fromSpatial(spatial);
    
    PhysicsControl control = (PhysicsControl) spatial.getControl(PhysicsControl.class);
    msg.setBodyType(control.getBodyDef().type);
    msg.setLinearDamping(control.getBodyDef().linearDamping);
    msg.setShapeType(control.getFixtureDef().shape.m_type);
    if (msg.getShapeType() == ShapeType.CIRCLE) {
      CircleShape circle = (CircleShape) control.getFixtureDef().shape;
      msg.setRadius(circle.m_radius);
    } else if (msg.getShapeType() == ShapeType.POLYGON) {
      PolygonShape polygon = (PolygonShape) control.getFixtureDef().shape;
      int numVertices = polygon.m_vertexCount;
      msg.centroid = new float[] {polygon.m_centroid.x, polygon.m_centroid.y};
      msg.vertices = new float[numVertices][2];
      msg.normals = new float[numVertices][2];
      for (int i = 0; i < numVertices; i++) {
        msg.vertices[i][0] = polygon.m_vertices[i].x;
        msg.vertices[i][1] = polygon.m_vertices[i].y;
        msg.normals[i][0] = polygon.m_normals[i].x;
        msg.normals[i][1] = polygon.m_normals[i].y;
      }
    }
    
    PhysicsMoveControl moveControl = (PhysicsMoveControl) spatial.getControl(PhysicsMoveControl.class);
    if (moveControl != null) {
      msg.setMoveSpeed(moveControl.getSpeed());
    }
    
    return msg;
  }
}
