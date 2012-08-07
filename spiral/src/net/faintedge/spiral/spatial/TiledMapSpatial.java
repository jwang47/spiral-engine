package net.faintedge.spiral.spatial;


import net.faintedge.spiral.core.Camera;
import net.faintedge.spiral.core.GameContext;
import net.faintedge.spiral.core.Spatial;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;

public class TiledMapSpatial extends Spatial {

  public static final Color SELECTION_COLOR = new Color(255, 175, 175, 96);
  public static final Color SELECTION_BORDER_COLOR = new Color(255, 175, 175, 180);
  // testing
  private int curTileX = -1;
  private int curTileY = -1;
  
  private TiledMap map;
  private GameContext context;
  
  public TiledMapSpatial(GameContext context, TiledMap map) {
    this.context = context;
    this.map = map;
  }

  public int worldToTileX(float x) {
    float mapWidth = map.getWidth() * map.getTileWidth();
    
    // the offset from the top left corner of this map for this point
    float offsetXFromTopLeft = x + mapWidth/2;
    
    return (int) (offsetXFromTopLeft / map.getTileWidth());
  }
  
  @Override
  protected void subUpdate(int delta) {
    float worldX = context.screenToWorldX(Mouse.getX());
    float worldY = context.screenToWorldY(Mouse.getY());
    
    curTileX = worldToTileX(worldX);
    curTileY = worldToTileX(worldY);

    curTileX = Math.max(0, Math.min(map.getWidth() - 1, curTileX));
    curTileY = Math.max(0, Math.min(map.getHeight() - 1, curTileY));
  }
  
  public int worldToTileY(float y) {
    float mapHeight = map.getHeight() * map.getTileHeight();
    
    // the offset from the top left corner of this map for this point
    float offsetYFromTopLeft = y + mapHeight/2;
    
    return (int) (offsetYFromTopLeft / map.getTileHeight());
  }

  @Override
  protected void subRender(Graphics g) {
    GameContainer container = context.getContainer();
    Camera camera = context.getCamera();
    
    int mapWidth = map.getWidth() * map.getTileWidth();
    int mapHeight = map.getHeight() * map.getTileHeight();
    
    int containerWidth = container.getWidth();
    int containerHeight = container.getHeight();

    float screenNumTilesWide = ((float) containerWidth) / map.getTileWidth();
    float screenNumTilesHigh = ((float) containerHeight) / map.getTileHeight();
    
    int cameraTileX = worldToTileX(camera.getTranslation().getX());
    int cameraTileY = worldToTileY(camera.getTranslation().getY());

    int sx = (int) (cameraTileX - screenNumTilesWide/2);
    int sy = (int) (cameraTileY - screenNumTilesHigh/2);
    int ex = (int) (cameraTileX + screenNumTilesWide/2) + 2;
    int ey = (int) (cameraTileY + screenNumTilesHigh/2) + 2;
    
    map.render(-mapWidth/2 + sx * map.getTileWidth(), -mapHeight/2 + sy * map.getTileHeight(), sx, sy, ex - sx, ey - sy);
    
    // now render test
    g.setColor(SELECTION_COLOR);
    g.fillRect(
        -mapWidth/2 + curTileX * map.getTileWidth(),
        -mapHeight/2 + curTileY * map.getTileHeight(),
        map.getTileWidth(), map.getTileHeight());
    g.setColor(SELECTION_BORDER_COLOR);
    g.drawRect(
        -mapWidth/2 + curTileX * map.getTileWidth(),
        -mapHeight/2 + curTileY * map.getTileHeight(),
        map.getTileWidth(), map.getTileHeight());
  }

}
