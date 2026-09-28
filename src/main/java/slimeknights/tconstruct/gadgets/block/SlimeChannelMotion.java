package slimeknights.tconstruct.gadgets.block;  
import net.minecraft.util.EnumFacing;


class ChannelMotion {

  public double x, y, z;

  public ChannelMotion() {
    x = 0;
    y = 0;
    z = 0;
  }

  public ChannelMotion boost(EnumFacing facing, double speed) {
    switch(facing) {
      case UP:
        this.y += speed * 3; // compensate for gravity
        break;
      case DOWN:
        this.y -= speed;
        break;
      case NORTH:
        this.z -= speed;
        break;
      case SOUTH:
        this.z += speed;
        break;
      case WEST:
        this.x -= speed;
        break;
      case EAST:
        this.x += speed;
        break;
    }
    return this;
  }
}
