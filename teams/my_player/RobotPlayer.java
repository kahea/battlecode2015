package player1;
import battlecode.common.*;
import java.util.*;
import java.util.Random; // this should give the same import 

public class RobotPlayer {

	static Direction facing;
	static Random rand; 

	public static void run(RobotController rc){
		rand = new Random(rc.getID()); 
		facing = Direction.values()[(int)(rand.nextDouble()*8)]; // randomize starting direction 
		while (true){
			try {
				// HQ can spawn units 
				if (rc.getType() == RobotType.HQ){
					if (rc.isCoreReady() && rc.canSpawn(Direction.NORTH, RobotType.BEAVER)){
						rc.spawn(Direction.NORTH, RobotType.BEAVER);
					}
				} else if (rc.getType() == RobotType.BEAVER) { 

					if (rc.senseOre(rc.getLocation()) > 1){
						rc.mine(); // then mine, there is ore 
					} else { 
						if (rand.nextDouble() < 0.05){
							if (rand.nextDouble() < 0.05) {
								facing = facing.rotateLeft();
							} else {
								facing = facing.rotateRight();
							}
						}

						if (rc.isCoreReady() && rc.canMove(facing)){
							rc.move(facing);
						}

					}
				}
			
			} catch (GameActionException e) {
				e.printStackTrace();
			}
			rc.yield();
		}
	} 
}