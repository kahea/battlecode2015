package player1;
import battlecode.common.*;
import java.util.*;

public class RobotPlayer {

	public static void run(RobotController rc){
		
		while (true){
			try {
			// HQ can spawn units 
				if (rc.getType() == RobotType.HQ){
					if (rc.isCoreReady() && rc.canSpawn(Direction.NORTH, RobotType.BEAVER)){
						rc.spawn(Direction.NORTH, RobotType.BEAVER);
					}
				} else {
					rc.move(Direction.NORTH);
				}
			} catch (GameActionException e) {
				if (rc.isCoreReady() && rc.canMove(Direction.North)){
					
				}
				e.printStackTrace();
			}
			rc.yield();
		}

	}

}