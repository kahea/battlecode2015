package player1;
import battlecode.common.*;
import java.util.*;
import java.util.Random; // this should give the same import 

public class RobotPlayer {

	static Direction facing;
	static Random rand; 
	static RobotController rc;

	public static void run(RobotController myrc){
		rand = new Random(myrc.getID()); // each robot will follow its own random path
		facing = Direction.values()[(int)(rand.nextDouble()*8)]; // randomize starting direction 
		while (true){
			try {
				// HQ can spawn units 
				if (myrc.getType() == RobotType.HQ){
					if (myrc.isCoreReady() && myrc.canSpawn(Direction.NORTH, RobotType.BEAVER)){
						myrc.spawn(Direction.NORTH, RobotType.BEAVER);
					}
				} else if (myrc.getType() == RobotType.BEAVER) { 
					// check if there is ore in current location 	
					if (myrc.senseOre(myrc.getLocation()) > 1){
						myrc.mine(); // if so, then mine it 
					} else { 
						moveAround();

						if (myrc.isCoreReady() && myrc.canMove(facing)){
							myrc.move(facing);
						}

					}
				}
			
			} catch (GameActionException e) {
				e.printStackTrace();
			}
			myrc.yield();
		}
	}
	private static void moveAround(){
		if (rand.nextDouble() < 0.05){
			if (rand.nextDouble() < 0.05) {
				facing = facing.rotateLeft();
			} else {
				facing = facing.rotateRight();
			}
		}
	} 
}