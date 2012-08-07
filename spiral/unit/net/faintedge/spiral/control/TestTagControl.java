package net.faintedge.spiral.control;
import static org.junit.Assert.*;

import net.faintedge.spiral.spatial.control.TagControl;

import org.junit.Test;


public class TestTagControl {

  @Test
  public void testHasTag() {
    TagControl tags = new TagControl("hello");
    assertTrue(tags.hasTag("hello"));
  }

}
